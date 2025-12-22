package com.example.bssm_dev.domain.auth.dto.request;

public record GoogleTokenRequest(
        String code,
        String clientId,
        String clientSecret,
        String redirectUri,
        String grantType
) {}