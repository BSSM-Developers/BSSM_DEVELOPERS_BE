package com.example.bssm_dev.domain.api.extractor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class RequestExtractor {
    public static String extractEndpoint(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String endpoint = requestUri.substring("/api/proxy".length());
        log.info("요청 URI = {}", requestUri);
        log.info("endpoint = {}", endpoint);
        return endpoint;
    }

    public static Map<String, String> extractHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Iterator<String> headers  = headerNames.asIterator();
        Map<String, String> headerMap = new HashMap<>();

        while (headers.hasNext()) {
            String headerName =  headers.next();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return  headerMap;
    }
}
