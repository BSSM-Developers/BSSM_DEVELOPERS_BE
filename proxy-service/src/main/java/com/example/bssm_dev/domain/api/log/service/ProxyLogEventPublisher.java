package com.example.bssm_dev.domain.api.log.service;

import com.example.bssm_dev.domain.api.event.ProxyLogEvent;
import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog;
import com.example.bssm_dev.domain.api.log.model.ProxyResult;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiTokenR2dbc;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyRequestLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyResponseLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyOriginLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyErrorLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
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
            ApiTokenR2dbc apiToken,
            RequestInfo requestInfo,
            ServerHttpRequest request,
            Object responseBody,
            long startedAtMillis
    ) {
        ProxyReqResLog document = buildBase(direction, apiToken.getApiTokenId(), apiToken.getUserId(), requestInfo, request, startedAtMillis, ProxyResult.SUCCESS);
        ProxyResponseLog responseLog = buildResponseLog(200, responseBody);
        document = ProxyReqResLog.builder()
                .id(document.getId())
                .traceId(document.getTraceId())
                .timestamp(document.getTimestamp())
                .timezone(document.getTimezone())
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
            ApiTokenR2dbc apiToken,
            RequestInfo requestInfo,
            ServerHttpRequest request,
            Throwable throwable,
            long startedAtMillis
    ) {
        ProxyReqResLog document = buildBase(direction, apiToken.getApiTokenId(), apiToken.getUserId(), requestInfo, request, startedAtMillis, ProxyResult.ERROR);

        int status = 500;
        String responseBody = null;
        if (throwable instanceof ExternalApiException externalApiException && externalApiException.getUpstreamStatusCode() != null) {
            status = externalApiException.getUpstreamStatusCode();
            responseBody = externalApiException.getUpstreamBody();
        }

        ProxyResponseLog responseLog = buildResponseLog(status, responseBody);
        ProxyErrorLog errorLog = buildErrorLog(throwable, status);

        document = ProxyReqResLog.builder()
                .id(document.getId())
                .traceId(document.getTraceId())
                .timestamp(document.getTimestamp())
                .timezone(document.getTimezone())
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
            Long tokenId,
            Long userId,
            RequestInfo requestInfo,
            ServerHttpRequest request,
            long startedAtMillis,
            ProxyResult result
    ) {
        ProxyRequestLog requestLog = buildRequestLog(requestInfo, request);
        ProxyOriginLog originLog = buildOrigin(request);

        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        return ProxyReqResLog.builder()
                .traceId(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .timezone(zoneId.getId())
                .direction(direction)
                .request(requestLog)
                .latencyMs(System.currentTimeMillis() - startedAtMillis)
                .tokenId(tokenId)
                .userId(userId)
                .origin(originLog)
                .result(result)
                .build();
    }

    private ProxyRequestLog buildRequestLog(RequestInfo requestInfo, ServerHttpRequest request) {
        Map<String, String> headers = sanitizeHeaders(requestInfo.headers());
        TruncatedBody truncatedBody = truncateBody(requestInfo.body());
        return ProxyRequestLog.builder()
                .method(request.getMethod() != null ? request.getMethod().name() : null)
                .scheme(request.getURI().getScheme())
                .host(request.getURI().getHost())
                .path(request.getURI().getPath())
                .query(request.getURI().getQuery())
                .headers(headers)
                .ip(request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : null)
                .userAgent(request.getHeaders().getFirst("User-Agent"))
                .body(truncatedBody.body())
                .bodyTruncated(truncatedBody.truncated())
                .contentLength(truncatedBody.contentLength())
                .build();
    }

    private ProxyOriginLog buildOrigin(ServerHttpRequest request) {
        return ProxyOriginLog.builder()
                .referer(request.getHeaders().getFirst("Referer"))
                .forwardedFor(request.getHeaders().getFirst("X-Forwarded-For"))
                .geo(request.getHeaders().getFirst("CF-IPCountry"))
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

    private ProxyErrorLog buildErrorLog(Throwable throwable, Integer code) {
        if (throwable == null) {
            return null;
        }
        return ProxyErrorLog.builder()
                .type(throwable.getClass().getSimpleName())
                .message(throwable.getMessage())
                .code(code)
                .stack(throwable.getStackTrace().length > 0 ? throwable.getStackTrace()[0].toString() : null)
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
