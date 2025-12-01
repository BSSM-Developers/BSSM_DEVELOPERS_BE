package com.example.bssm_dev.domain.auth.dto.request;

public record GoogleTokenExchangeRequest(
        String code,
        String codeVerifier
) {
}
