package com.example.bssm_dev.domain.api.dto.response;

import java.util.List;

public record SecretApiTokenResponse(
        Long apiTokenId,
        String apiTokenName,
        String apiTokenClientId,
        String secretKey,
        List<String> domains
) {
}

