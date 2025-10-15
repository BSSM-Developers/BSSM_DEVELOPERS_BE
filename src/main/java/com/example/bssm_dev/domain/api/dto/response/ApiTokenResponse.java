package com.example.bssm_dev.domain.api.dto.response;

public record ApiTokenResponse(
        Long apiTokenId,
        Long userId,
        String secretKey
) {
}
