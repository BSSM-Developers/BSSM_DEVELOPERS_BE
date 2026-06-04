package com.example.bssm_dev.domain.github.dto.response;

import com.example.bssm_dev.domain.api.model.Api;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AST 파싱으로 감지된 엔드포인트")
public record ParsedEndpointResponse(
        @Schema(description = "HTTP 메서드", example = "GET")
        String method,

        @Schema(description = "엔드포인트 경로", example = "/health")
        String endpoint
) {
    public static ParsedEndpointResponse from(Api api) {
        return new ParsedEndpointResponse(api.getMethod(), api.getEndpoint());
    }

    public static ParsedEndpointResponse from(EndpointParseResponse.ParsedEndpoint parsed) {
        return new ParsedEndpointResponse(parsed.method(), parsed.path());
    }
}
