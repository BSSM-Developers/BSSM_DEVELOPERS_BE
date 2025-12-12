package com.example.bssm_dev.domain.api.log.service;

import com.example.bssm_dev.domain.api.event.ProxyLogEvent;
import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog;
import com.example.bssm_dev.domain.api.log.model.ProxyResult;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyRequestLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyResponseLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyOriginLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyErrorLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProxyLogEventPublisher {
    private static final int MAX_BODY_LENGTH = 2048;

    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    public void publishSuccess(
            ProxyLogDirection direction,
            ApiToken apiToken,
            RequestInfo requestInfo,
            HttpServletRequest servletRequest,
            Object responseBody,
            long startedAtMillis
    ) {
        ProxyReqResLog document = buildBase(direction, apiToken, requestInfo, servletRequest, startedAtMillis, ProxyResult.SUCCESS);
        ProxyResponseLog responseLog = buildResponseLog(200, responseBody);
        document = ProxyReqResLog.builder()
                .id(document.getId())
                .traceId(document.getTraceId())
                .timestamp(document.getTimestamp())
                .direction(document.getDirection())
                .request(document.getRequest())
                .response(responseLog)
                .latencyMs(document.getLatencyMs())
                .tokenId(document.getTokenId())
                .userId(document.getUserId())
                .origin(document.getOrigin())
                .result(document.getResult())
                .error(document.getError())
                .build();
        eventPublisher.publishEvent(new ProxyLogEvent(document));
    }

    public void publishError(
            ProxyLogDirection direction,
            ApiToken apiToken,
            RequestInfo requestInfo,
            HttpServletRequest servletRequest,
            Exception exception,
            long startedAtMillis
    ) {
        ProxyReqResLog document = buildBase(direction, apiToken, requestInfo, servletRequest, startedAtMillis, ProxyResult.ERROR);

        int status = 500;
        String responseBody = null;
        if (exception instanceof ExternalApiException externalApiException && externalApiException.getUpstreamStatusCode() != null) {
            status = externalApiException.getUpstreamStatusCode();
            responseBody = externalApiException.getUpstreamBody();
        }

        ProxyResponseLog responseLog = buildResponseLog(status, responseBody);
        ProxyErrorLog errorLog = buildErrorLog(exception, status);

        document = ProxyReqResLog.builder()
                .id(document.getId())
                .traceId(document.getTraceId())
                .timestamp(document.getTimestamp())
                .direction(document.getDirection())
                .request(document.getRequest())
                .response(responseLog)
                .latencyMs(document.getLatencyMs())
                .tokenId(document.getTokenId())
                .userId(document.getUserId())
                .origin(document.getOrigin())
                .result(document.getResult())
                .error(errorLog)
                .build();

        eventPublisher.publishEvent(new ProxyLogEvent(document));
    }

    private ProxyReqResLog buildBase(
            ProxyLogDirection direction,
            ApiToken apiToken,
            RequestInfo requestInfo,
            HttpServletRequest servletRequest,
            long startedAtMillis,
            ProxyResult result
    ) {
        ProxyRequestLog requestLog = buildRequestLog(requestInfo, servletRequest);
        ProxyOriginLog originLog = buildOrigin(servletRequest);

        return ProxyReqResLog.builder()
                .traceId(UUID.randomUUID().toString())
                .timestamp(ZonedDateTime.now(ZoneId.of("Asia/Seoul")))
                .direction(direction)
                .request(requestLog)
                .latencyMs(System.currentTimeMillis() - startedAtMillis)
                .tokenId(apiToken.getApiTokenId())
                .userId(apiToken.getUser() != null ? apiToken.getUser().getUserId() : null)
                .origin(originLog)
                .result(result)
                .build();
    }

    private ProxyRequestLog buildRequestLog(RequestInfo requestInfo, HttpServletRequest servletRequest) {
        Map<String, String> headers = sanitizeHeaders(requestInfo.headers());
        TruncatedBody truncatedBody = truncateBody(requestInfo.body());
        return ProxyRequestLog.builder()
                .method(servletRequest.getMethod())
                .scheme(servletRequest.getScheme())
                .host(servletRequest.getServerName())
                .path(servletRequest.getRequestURI())
                .query(servletRequest.getQueryString())
                .headers(headers)
                .ip(servletRequest.getRemoteAddr())
                .userAgent(servletRequest.getHeader("User-Agent"))
                .body(truncatedBody.body())
                .bodyTruncated(truncatedBody.truncated())
                .contentLength(truncatedBody.contentLength())
                .build();
    }

    private ProxyOriginLog buildOrigin(HttpServletRequest servletRequest) {
        return ProxyOriginLog.builder()
                .referer(servletRequest.getHeader("Referer"))
                .forwardedFor(servletRequest.getHeader("X-Forwarded-For"))
                .geo(servletRequest.getHeader("CF-IPCountry"))
                .build();
    }

    private ProxyResponseLog buildResponseLog(Integer status, Object responseBody) {
        TruncatedBody truncatedBody = truncateBody(responseBody);
        return ProxyResponseLog.builder()
                .status(status)
                .headers(Collections.emptyMap())
                .body(truncatedBody.body())
                .bodyTruncated(truncatedBody.truncated())
                .contentLength(truncatedBody.contentLength())
                .build();
    }

    private ProxyErrorLog buildErrorLog(Exception exception, Integer code) {
        if (exception == null) {
            return null;
        }
        return ProxyErrorLog.builder()
                .type(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .code(code)
                .stack(exception.getStackTrace().length > 0 ? exception.getStackTrace()[0].toString() : null)
                .build();
    }

    private Map<String, String> sanitizeHeaders(Map<String, String> headers) {
        if (headers == null) {
            return Collections.emptyMap();
        }
        Map<String, String> safeHeaders = new HashMap<>();
        headers.forEach((key, value) -> {
            if (key == null) {
                return;
            }
            if (isSensitiveHeader(key)) {
                safeHeaders.put(key, mask(value));
            } else {
                safeHeaders.put(key, value);
            }
        });
        return safeHeaders;
    }

    private boolean isSensitiveHeader(String headerName) {
        String lower = headerName.toLowerCase();
        return lower.contains("authorization")
                || lower.contains("cookie")
                || lower.contains("token")
                || lower.contains("secret")
                || lower.contains("credential");
    }

    private String mask(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        if (value.length() <= 6) {
            return "***";
        }
        String prefix = value.substring(0, 3);
        String suffix = value.substring(value.length() - 3);
        return prefix + "***" + suffix;
    }

    private TruncatedBody truncateBody(byte[] bodyBytes) {
        if (bodyBytes == null) {
            return new TruncatedBody(null, false, 0L);
        }
        String asString = new String(bodyBytes, StandardCharsets.UTF_8);
        return truncateBody(asString);
    }

    private TruncatedBody truncateBody(Object body) {
        if (body == null) {
            return new TruncatedBody(null, false, 0L);
        }
        if (body instanceof byte[] bytes) {
            return truncateBody(bytes);
        }
        String bodyString;
        if (body instanceof String s) {
            bodyString = s;
        } else {
            try {
                bodyString = objectMapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                bodyString = body.toString();
            }
        }
        return truncateBody(bodyString);
    }

    private TruncatedBody truncateBody(String body) {
        if (body == null) {
            return new TruncatedBody(null, false, 0L);
        }
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        boolean truncated = bytes.length > MAX_BODY_LENGTH;
        String value = body;
        if (truncated) {
            value = new String(bytes, 0, MAX_BODY_LENGTH, StandardCharsets.UTF_8);
        }
        return new TruncatedBody(value, truncated, (long) bytes.length);
    }

    private record TruncatedBody(String body, boolean truncated, Long contentLength) {
    }
}
