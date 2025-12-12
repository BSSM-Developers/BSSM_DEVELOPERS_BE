package com.example.bssm_dev.domain.api.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "proxy_logs")
public class ProxyReqResLog {
    @Id
    private String id;
    private String traceId;
    private ZonedDateTime timestamp;
    private ProxyLogDirection direction;
    private ProxyRequestLog request;
    private ProxyResponseLog response;
    private Long latencyMs;
    private Long tokenId;
    private Long userId;
    private ProxyOriginLog origin;
    private ProxyResult result;
    private ProxyErrorLog error;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProxyRequestLog {
        private String method;
        private String scheme;
        private String host;
        private String path;
        private String query;
        private Map<String, String> headers;
        private String ip;
        private String userAgent;
        private String body;
        private boolean bodyTruncated;
        private Long contentLength;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProxyResponseLog {
        private Integer status;
        private Map<String, String> headers;
        private String body;
        private boolean bodyTruncated;
        private Long contentLength;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProxyOriginLog {
        private String referer;
        private String forwardedFor;
        private String geo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProxyErrorLog {
        private String type;
        private String message;
        private Integer code;
        private String stack;
    }
}
