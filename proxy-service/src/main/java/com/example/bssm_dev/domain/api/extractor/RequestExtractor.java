package com.example.bssm_dev.domain.api.extractor;

import com.example.bssm_dev.domain.api.exception.UnsupportedProxyBasePathException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestExtractor {
    public static String extractEndpoint(ServerHttpRequest request) {
        String requestUri = request.getURI().getPath();
        String queryString = request.getURI().getQuery();
        String endpoint = extractEndpoint(requestUri, queryString);

        if (endpoint.startsWith("//")) {
            endpoint = endpoint.replaceFirst("^/+", "/");
        }

        log.debug("요청 URI = {}", requestUri);
        log.debug("쿼리 파라미터 = {}", queryString);
        log.debug("기준 경로 = {}", resolveBasePath(requestUri));
        log.debug("최종 endpoint = {}", endpoint);
        return endpoint;
    }

    public static Map<String, String> extractHeaders(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        Map<String, String> headerMap = new HashMap<>();
        headers.forEach((headerName, values) -> {
            if (headerName == null) {
                return;
            }
            if (headerName.equalsIgnoreCase("bssm-dev-token")
                    || headerName.equalsIgnoreCase("bssm-dev-secret")) {
                return;
            }
            if (headerName.equalsIgnoreCase("host")) {
                return;
            }
            String value = values != null && !values.isEmpty() ? values.get(0) : null;
            headerMap.put(headerName, value);
        });
        return headerMap;
    }

    private static String extractEndpoint(String requestUri, String queryString) {
        String basePath = resolveBasePath(requestUri);
        String endpoint = requestUri.substring(basePath.length());
        if (queryString != null && !queryString.isEmpty()) {
            endpoint = endpoint + "?" + queryString;
        }
        return endpoint;
    }

    private static String resolveBasePath(String requestUri) {
        if (requestUri.startsWith("/proxy-browser")) {
            return "/proxy-browser";
        }
        if (requestUri.startsWith("/proxy-server")) {
            return "/proxy-server";
        }
        throw UnsupportedProxyBasePathException.raise();
    }
}
