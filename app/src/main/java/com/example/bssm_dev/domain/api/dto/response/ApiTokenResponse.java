package com.example.bssm_dev.domain.api.dto.response;

import com.example.bssm_dev.domain.api.model.type.ApiTokenState;

import java.util.List;

public record ApiTokenResponse(
        Long apiTokenId,
        String apiTokenName,
        String apiTokenClientId,
        ApiTokenState state,
        List<String> domains,
        List<ApiUsageSummaryResponse> registeredApis
) {
}

