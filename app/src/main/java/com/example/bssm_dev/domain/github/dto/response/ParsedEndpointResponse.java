package com.example.bssm_dev.domain.github.dto.response;

import com.example.bssm_dev.domain.api.model.Api;

public record ParsedEndpointResponse(
        String method,
        String endpoint
) {
    public static ParsedEndpointResponse from(Api api) {
        return new ParsedEndpointResponse(api.getMethod(), api.getEndpoint());
    }
}
