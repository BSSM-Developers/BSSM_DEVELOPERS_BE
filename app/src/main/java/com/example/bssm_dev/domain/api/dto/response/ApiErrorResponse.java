package com.example.bssm_dev.domain.api.dto.response;

public record ApiErrorResponse (
        int statusCode,
        String message,
        String cause
) {
}
