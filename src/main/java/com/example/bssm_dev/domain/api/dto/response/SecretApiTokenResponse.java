package com.example.bssm_dev.domain.api.dto.response;

public record SecretApiTokenResponse(
        Long apiTokenId,
        String secretKey
) {
}
