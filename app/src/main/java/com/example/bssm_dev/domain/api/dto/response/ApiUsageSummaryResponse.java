package com.example.bssm_dev.domain.api.dto.response;

public record ApiUsageSummaryResponse(
        String apiId,
        String name,
        String endpoint,
        String apiMethod,
        String apiUseState
) {
}
