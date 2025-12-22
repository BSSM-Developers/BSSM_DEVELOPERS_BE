package com.example.bssm_dev.domain.auth.dto.response;

public record GoogleTokenResponse(
        String access_token,
        String refresh_token,
        String scope,
        String token_type,
        Long expires_in,
        String id_token
) {}
