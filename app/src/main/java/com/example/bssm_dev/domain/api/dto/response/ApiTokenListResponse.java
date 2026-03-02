package com.example.bssm_dev.domain.api.dto.response;

import com.example.bssm_dev.domain.api.model.type.ApiTokenState;

public record ApiTokenListResponse(
        Long apiTokenId,
        String apiTokenName,
        String apiTokenClientId,
        ApiTokenState state
) {
}
