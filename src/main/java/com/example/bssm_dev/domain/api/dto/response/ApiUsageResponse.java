package com.example.bssm_dev.domain.api.dto.response;

public record ApiUsageResponse(
        Long apiTokenId,
        Long apiId,
        String name,
        String endpoint,
        String apiName,
        String apiDomain,
        String apiMethod,
        String apiUseReasonId,
        String apiUseState
) {
}
