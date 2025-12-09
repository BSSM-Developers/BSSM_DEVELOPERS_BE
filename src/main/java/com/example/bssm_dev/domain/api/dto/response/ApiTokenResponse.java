package com.example.bssm_dev.domain.api.dto.response;

import com.example.bssm_dev.domain.api.model.TokenType;

import java.util.List;

public record ApiTokenResponse(
        Long apiTokenId,
        String apiTokenName,
        String apiTokenClientId,
        TokenType tokenType,
        List<String> domains,
        List<ApiUsageSummaryResponse> registeredApis
) {
}

