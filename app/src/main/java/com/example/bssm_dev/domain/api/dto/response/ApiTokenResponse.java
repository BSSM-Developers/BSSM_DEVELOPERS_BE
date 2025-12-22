package com.example.bssm_dev.domain.api.dto.response;

import java.util.List;

public record ApiTokenResponse(
        Long apiTokenId,
        String apiTokenName,
        String apiTokenClientId,
        List<String> domains,
        List<ApiUsageSummaryResponse> registeredApis
) {
}

