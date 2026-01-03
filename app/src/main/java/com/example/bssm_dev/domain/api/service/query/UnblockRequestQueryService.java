package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.api.dto.response.UnblockRequestResponse;
import com.example.bssm_dev.domain.api.exception.UnauthorizedUnblockRequestAccessException;
import com.example.bssm_dev.domain.api.exception.UnblockRequestNotFoundException;
import com.example.bssm_dev.domain.api.mapper.UnblockRequestMapper;
import com.example.bssm_dev.domain.api.model.UnblockRequest;
import com.example.bssm_dev.domain.api.model.type.UnblockRequestState;
import com.example.bssm_dev.domain.api.repository.UnblockRequestRepository;
import com.example.bssm_dev.domain.user.exception.UnauthorizedException;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.model.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(value = "transactionManager", readOnly = true)
public class UnblockRequestQueryService {
    private final UnblockRequestRepository unblockRequestRepository;
    private final UnblockRequestMapper unblockRequestMapper;

    public UnblockRequestResponse getUnblockRequest(User user, Long unblockRequestId) {
        UnblockRequest unblockRequest = unblockRequestRepository.findById(unblockRequestId)
                .orElseThrow(UnblockRequestNotFoundException::raise);

        // 요청자 본인이거나 관리자만 조회 가능
        if (!unblockRequest.isRequester(user) && user.getRole() != UserRole.ROLE_ADMIN) {
            throw UnauthorizedUnblockRequestAccessException.raise();
        }

        return unblockRequestMapper.toResponse(unblockRequest);
    }

    public CursorPage<UnblockRequestResponse> getMyUnblockRequests(User user, Long cursor, Integer size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<UnblockRequest> unblockRequestSlice = unblockRequestRepository
                .findByRequesterWithCursor(user, cursor, pageable);
        List<UnblockRequestResponse> responses = unblockRequestMapper.toResponseList(
                unblockRequestSlice.getContent()
        );
        return new CursorPage<>(responses, unblockRequestSlice.hasNext());
    }

    public CursorPage<UnblockRequestResponse> getPendingUnblockRequests(User admin, Long cursor, Integer size) {
        validateAdmin(admin);

        Pageable pageable = PageRequest.of(0, size);
        Slice<UnblockRequest> unblockRequestSlice = unblockRequestRepository
                .findByStateWithCursor(UnblockRequestState.PENDING, cursor, pageable);
        List<UnblockRequestResponse> responses = unblockRequestMapper.toResponseList(
                unblockRequestSlice.getContent()
        );
        return new CursorPage<>(responses, unblockRequestSlice.hasNext());
    }

    private void validateAdmin(User user) {
        if (user.getRole() != UserRole.ROLE_ADMIN) {
            throw UnauthorizedException.raise();
        }
    }
}
