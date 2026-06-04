package com.example.bssm_dev.domain.api.dto.response;

public record ApiUseReasonResponse(
        Long apiUseReasonId,
        Long writerId,
        String apiUseReason,
        String apiUseState
) {
}
