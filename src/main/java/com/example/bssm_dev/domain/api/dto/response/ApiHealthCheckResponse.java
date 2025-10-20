package com.example.bssm_dev.domain.api.dto.response;

public record ApiHealthCheckResponse (
        boolean healthy,
        Object response
) {
    public static ApiHealthCheckResponse of(boolean healthy, Object response) {
        return new ApiHealthCheckResponse(healthy, response);
    }
}
