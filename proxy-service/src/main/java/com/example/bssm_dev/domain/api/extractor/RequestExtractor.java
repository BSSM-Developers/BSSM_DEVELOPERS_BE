package com.example.bssm_dev.domain.api.extractor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class RequestExtractor {
    public static final String BROWSER_BASE_PATH = "/proxy-browser";
    public static final String SERVER_BASE_PATH = "/proxy-server";

    private static final Set<String> FILTERED_REQUEST_HEADERS = Set.of(
            "bssm-dev-token", "bssm-dev-secret", "host"
    );

    private RequestExtractor() {}

    public static String extractEndpoint(ServerHttpRequest request) {
        String requestUri = request.getURI().getPath();
        String queryString = request.getURI().getQuery();

        String basePath = resolveBasePath(requestUri);
        String endpoint = requestUri.substring(basePath.length());
        if (queryString != null && !queryString.isEmpty()) {
            endpoint = endpoint + "?" + queryString;
        }
        if (endpoint.startsWith("//")) {
            endpoint = endpoint.replaceFirst("^/+", "/");
        }

        log.debug("requestUri={} query={} basePath={} endpoint={}", requestUri, queryString, basePath, endpoint);
        return endpoint;
    }

    public static Map<String, String> extractHeaders(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        Map<String, String> headerMap = new HashMap<>();
        headers.forEach((name, values) -> {
            if (name == null) return;
            if (FILTERED_REQUEST_HEADERS.contains(name.toLowerCase())) return;
            headerMap.put(name, values != null && !values.isEmpty() ? values.get(0) : null);
        });
        return headerMap;
    }

    private static String resolveBasePath(String requestUri) {
        if (requestUri.startsWith(BROWSER_BASE_PATH)) return BROWSER_BASE_PATH;
        if (requestUri.startsWith(SERVER_BASE_PATH)) return SERVER_BASE_PATH;
        return "";
    }
}
