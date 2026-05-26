package com.example.bssm_dev.domain.github.dto.response;

public record GitHubInstallationTokenResponse(
        String token,
        String expires_at
) {}
