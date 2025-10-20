package com.example.bssm_dev.domain.api.dto.response;

public record ApiHealthCheckResponse (
        boolean healthy
) {
    public static ApiHealthCheckResponse of(boolean healthy) {
        return new ApiHealthCheckResponse(healthy);
    }
}
