package com.example.bssm_dev.domain.api.dto.request;

public record ApiHealthCheckRequest (
        String endpoint,
        String method,
        String domain,
        Object body
) {
}
