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
        
        // 쿼리 파라미터가 있으면 추가
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            endpoint = endpoint + "?" + queryString;
        }
        
        log.info("요청 URI = {}", requestUri);
        log.info("쿼리 파라미터 = {}", queryString);
        log.info("최종 endpoint = {}", endpoint);
        return endpoint;
    }

    public static Map<String, String> extractHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Iterator<String> headers  = headerNames.asIterator();
        Map<String, String> headerMap = new HashMap<>();

        while (headers.hasNext()) {
            String headerName =  headers.next();
            
            // bssm-dev 내부 인증 헤더는 제외
            if (headerName.equalsIgnoreCase("bssm-dev-token") || 
                headerName.equalsIgnoreCase("bssm-dev-secret")) {
                continue;
            }
            
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return  headerMap;
    }
}
