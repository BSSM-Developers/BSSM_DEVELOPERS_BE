package com.example.bssm_dev.domain.github.dto.response;

import java.util.List;

public record EndpointParseResponse(
        List<ParsedEndpoint> endpoints
) {
    public record ParsedEndpoint(
            String method,
            String path
    ) {}
}
