package com.example.bssm_dev.domain.api.mapper;

import com.example.bssm_dev.domain.api.dto.response.UnblockRequestResponse;
import com.example.bssm_dev.domain.api.model.UnblockRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnblockRequestMapper {

    public UnblockRequestResponse toResponse(UnblockRequest unblockRequest) {
        return new UnblockRequestResponse(
                unblockRequest.getUnblockRequestId(),
                unblockRequest.getApiToken().getApiTokenId(),
                unblockRequest.getApiToken().getApiTokenName(),
                unblockRequest.getRequester().getName(),
                unblockRequest.getReason(),
                unblockRequest.getState(),
                unblockRequest.getReviewer() != null ? unblockRequest.getReviewer().getName() : null,
                unblockRequest.getReviewedAt(),
                unblockRequest.getRejectReason(),
                unblockRequest.getCreatedAt()
        );
    }

    public UnblockRequest toEntity(com.example.bssm_dev.domain.api.model.ApiToken apiToken, 
                                     com.example.bssm_dev.domain.user.model.User requester, 
                                     String reason) {
        return UnblockRequest.builder()
                .apiToken(apiToken)
                .requester(requester)
                .reason(reason)
                .build();
    }

    public List<UnblockRequestResponse> toResponseList(List<UnblockRequest> unblockRequests) {
        return unblockRequests.stream()
                .map(this::toResponse)
                .toList();
    }
}
