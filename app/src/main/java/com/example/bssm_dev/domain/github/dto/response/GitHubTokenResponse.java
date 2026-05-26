package com.example.bssm_dev.domain.github.dto.response;

public record GitHubTokenResponse(
        String access_token,
        String refresh_token,
        Long expires_in,
        Long refresh_token_expires_in,
        String scope,
        String token_type
) {}
