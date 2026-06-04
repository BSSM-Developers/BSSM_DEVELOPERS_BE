package com.example.bssm_dev.global.error;

public record ErrorResponse (
        int statusCode,
        String message
) {
}
