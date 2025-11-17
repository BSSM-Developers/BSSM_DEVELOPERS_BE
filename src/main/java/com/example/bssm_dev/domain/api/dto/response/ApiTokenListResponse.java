package com.example.bssm_dev.domain.api.dto.response;

public record ApiTokenListResponse(
        Long apiTokenId,
        String apiTokenName,
        String apiTokenClientId
) {
}
