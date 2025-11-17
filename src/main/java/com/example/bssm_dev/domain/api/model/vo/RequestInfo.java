package com.example.bssm_dev.domain.api.model.vo;

import com.example.bssm_dev.domain.api.extractor.RequestExtractor;
import com.example.bssm_dev.domain.api.model.type.MethodType;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Map;

public record RequestInfo (
        Map<String, String> headers,
        byte[] body,
        String endpoint,
        MethodType method
) {

    public static RequestInfo of(HttpServletRequest request) {
        try {
            Map<String, String> headers = RequestExtractor.extractHeaders(request);
            byte[] body = request.getInputStream().readAllBytes();
            String endpoint = RequestExtractor.extractEndpoint(request);
            MethodType method = MethodType.valueOf(request.getMethod());
            return new RequestInfo(headers, body, endpoint, method);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read request information", e);
        }
    }
}
