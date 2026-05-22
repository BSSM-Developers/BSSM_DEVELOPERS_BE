package com.example.bssm_dev.domain.api.log.service;

import com.example.bssm_dev.domain.api.event.ProxyLogEvent;
import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyErrorLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyOriginLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyRequestLog;
import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog.ProxyResponseLog;
import com.example.bssm_dev.domain.api.log.model.ProxyResult;
import com.example.bssm_dev.domain.api.log.util.BodyTruncator;
import com.example.bssm_dev.domain.api.log.util.LogHeaderSanitizer;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiTokenR2dbc;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProxyLogEventPublisher {
    private static final ZoneId ZONE_KST = ZoneId.of("Asia/Seoul");

    private final ApplicationEventPublisher eventPublisher;
    private final BodyTruncator bodyTruncator;

    public void publishSuccess(
            ProxyLogDirection direction,
            ApiTokenR2dbc apiToken,
            RequestInfo requestInfo,
            ServerHttpRequest request,
            Object responseBody,
            long startedAtMillis
    ) {
        ProxyReqResLog log = ProxyReqResLog.builder()
                .traceId(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .timezone(ZONE_KST.getId())
                .direction(direction)
                .request(buildRequestLog(requestInfo, request))
                .response(buildResponseLog(200, responseBody))
                .latencyMs(System.currentTimeMillis() - startedAtMillis)
                .tokenId(apiToken.getApiTokenId())
                .userId(apiToken.getUserId())
                .origin(buildOriginLog(request))
                .result(ProxyResult.SUCCESS)
                .build();
        eventPublisher.publishEvent(new ProxyLogEvent(log));
    }

    public void publishError(
            ProxyLogDirection direction,
            ApiTokenR2dbc apiToken,
            RequestInfo requestInfo,
            ServerHttpRequest request,
            Throwable throwable,
            long startedAtMillis
    ) {
        int status = 500;
        String upstreamBody = null;
        if (throwable instanceof ExternalApiException ex && ex.getUpstreamStatusCode() != null) {
            status = ex.getUpstreamStatusCode();
            upstreamBody = ex.getUpstreamBody();
        }

        ProxyReqResLog log = ProxyReqResLog.builder()
                .traceId(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .timezone(ZONE_KST.getId())
                .direction(direction)
                .request(buildRequestLog(requestInfo, request))
                .response(buildResponseLog(status, upstreamBody))
                .latencyMs(System.currentTimeMillis() - startedAtMillis)
                .tokenId(apiToken.getApiTokenId())
                .userId(apiToken.getUserId())
                .origin(buildOriginLog(request))
                .result(ProxyResult.ERROR)
                .error(buildErrorLog(throwable, status))
                .build();
        eventPublisher.publishEvent(new ProxyLogEvent(log));
    }

    private ProxyRequestLog buildRequestLog(RequestInfo requestInfo, ServerHttpRequest request) {
        Map<String, String> safeHeaders = LogHeaderSanitizer.sanitize(requestInfo.headers());
        BodyTruncator.Result body = bodyTruncator.truncate(requestInfo.body());
        return ProxyRequestLog.builder()
                .method(request.getMethod() != null ? request.getMethod().name() : null)
                .scheme(request.getURI().getScheme())
                .host(request.getURI().getHost())
                .path(request.getURI().getPath())
                .query(request.getURI().getQuery())
                .headers(safeHeaders)
                .ip(request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : null)
                .userAgent(request.getHeaders().getFirst("User-Agent"))
                .body(body.body())
                .bodyTruncated(body.truncated())
                .contentLength(body.contentLength())
                .build();
    }

    private ProxyResponseLog buildResponseLog(Integer status, Object responseBody) {
        BodyTruncator.Result body = bodyTruncator.truncate(responseBody);
        return ProxyResponseLog.builder()
                .status(status)
                .headers(Collections.emptyMap())
                .body(body.body())
                .bodyTruncated(body.truncated())
                .contentLength(body.contentLength())
                .build();
    }

    private ProxyOriginLog buildOriginLog(ServerHttpRequest request) {
        return ProxyOriginLog.builder()
                .referer(request.getHeaders().getFirst("Referer"))
                .forwardedFor(request.getHeaders().getFirst("X-Forwarded-For"))
                .geo(request.getHeaders().getFirst("CF-IPCountry"))
                .build();
    }

    private ProxyErrorLog buildErrorLog(Throwable throwable, Integer code) {
        if (throwable == null) return null;
        return ProxyErrorLog.builder()
                .type(throwable.getClass().getSimpleName())
                .message(throwable.getMessage())
                .code(code)
                .stack(throwable.getStackTrace().length > 0 ? throwable.getStackTrace()[0].toString() : null)
                .build();
    }
}
