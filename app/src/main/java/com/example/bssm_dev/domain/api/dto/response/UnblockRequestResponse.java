package com.example.bssm_dev.domain.api.dto.response;

import com.example.bssm_dev.domain.api.model.type.UnblockRequestState;

import java.time.LocalDateTime;

public record UnblockRequestResponse(
        Long unblockRequestId,
        Long apiTokenId,
        String apiTokenName,
        String requesterName,
        String reason,
        UnblockRequestState state,
        String reviewerName,
        LocalDateTime reviewedAt,
        String rejectReason,
        LocalDateTime createdAt
) {
}
