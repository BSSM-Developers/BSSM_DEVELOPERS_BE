package com.example.bssm_dev.domain.api.model.vo;

import com.example.bssm_dev.domain.api.extractor.RequestExtractor;
import com.example.bssm_dev.domain.api.model.type.MethodType;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Map;

public record RequestInfo (
        Map<String, String> headers,
        byte[] body,
        String endpoint,
        MethodType method
) {

    public static RequestInfo of(ServerHttpRequest request, byte[] body) {
        Map<String, String> headers = RequestExtractor.extractHeaders(request);
        String endpoint = RequestExtractor.extractEndpoint(request);
        MethodType method = MethodType.valueOf(request.getMethod().name());
        return new RequestInfo(headers, body, endpoint, method);
    }
}
