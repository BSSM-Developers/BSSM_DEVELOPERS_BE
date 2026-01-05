package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.dto.response.UnblockRequestResponse;
import com.example.bssm_dev.domain.api.exception.*;
import com.example.bssm_dev.domain.api.mapper.UnblockRequestMapper;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.UnblockRequest;
import com.example.bssm_dev.domain.api.model.type.ApiTokenState;
import com.example.bssm_dev.domain.api.model.type.UnblockRequestState;
import com.example.bssm_dev.domain.api.repository.ApiTokenRepository;
import com.example.bssm_dev.domain.api.repository.UnblockRequestRepository;
import com.example.bssm_dev.domain.user.exception.UnauthorizedException;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.model.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class UnblockRequestCommandService {
    private final UnblockRequestRepository unblockRequestRepository;
    private final ApiTokenRepository apiTokenRepository;
    private final UnblockRequestMapper unblockRequestMapper;

    public void createUnblockRequest(User user, Long apiTokenId, String reason) {
        ApiToken apiToken = apiTokenRepository.findById(apiTokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        // 소유자 확인
        if (!apiToken.isOwner(user)) {
            throw UnauthorizedApiTokenAccessException.raise();
        }

        // 차단된 토큰인지 확인
        if (apiToken.getState() != ApiTokenState.BLOCKED) {
            throw ApiTokenNotBlockedException.raise();
        }

        // 이미 대기 중인 요청이 있는지 확인
        boolean hasPendingRequest = unblockRequestRepository
                .existsByApiTokenAndState(apiToken, UnblockRequestState.PENDING);
        if (hasPendingRequest) {
            throw UnblockRequestAlreadyExistsException.raise();
        }

        UnblockRequest unblockRequest = unblockRequestMapper.toEntity(apiToken, user, reason);
        unblockRequestRepository.save(unblockRequest);
    }

    public void approveUnblockRequest(User admin, Long unblockRequestId) {
        validateAdmin(admin);

        UnblockRequest unblockRequest = unblockRequestRepository.findById(unblockRequestId)
                .orElseThrow(UnblockRequestNotFoundException::raise);

        if (!unblockRequest.isPending()) {
            throw UnblockRequestAlreadyProcessedException.raise();
        }

        unblockRequest.approve(admin);

        // API Token 상태를 NORMAL로 리셋
        ApiToken apiToken = unblockRequest.getApiToken();
        apiToken.unblock();
        apiTokenRepository.save(apiToken);
    }

    public void rejectUnblockRequest(User admin, Long unblockRequestId, String rejectReason) {
        validateAdmin(admin);

        UnblockRequest unblockRequest = unblockRequestRepository.findById(unblockRequestId)
                .orElseThrow(UnblockRequestNotFoundException::raise);

        if (!unblockRequest.isPending()) {
            throw UnblockRequestAlreadyProcessedException.raise();
        }

        unblockRequest.reject(admin, rejectReason);
    }

    private void validateAdmin(User user) {
        if (!user.isAdmin()) {
            throw UnauthorizedException.raise();
        }
    }
}
