package com.example.bssm_dev.domain.api.dto.response;

import com.example.bssm_dev.domain.api.model.TokenType;

import java.util.List;

public record SecretApiTokenResponse(
        Long apiTokenId,
        String apiTokenName,
        String apiTokenClientId,
        String secretKey,
        TokenType tokenType,
        List<String> domains
) {
}

