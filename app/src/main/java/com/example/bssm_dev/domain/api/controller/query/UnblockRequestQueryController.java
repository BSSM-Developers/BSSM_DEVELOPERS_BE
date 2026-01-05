package com.example.bssm_dev.domain.api.controller.query;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.response.UnblockRequestResponse;
import com.example.bssm_dev.domain.api.service.query.UnblockRequestQueryService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UnblockRequestQueryController {
    private final UnblockRequestQueryService unblockRequestQueryService;

    /**
     * 내 차단 해제 요청 목록 조회
     */
    @GetMapping("/api/token/unblock-requests")
    public ResponseEntity<ResponseDto<CursorPage<UnblockRequestResponse>>> getMyUnblockRequests(
            @CurrentUser User user,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        CursorPage<UnblockRequestResponse> response = unblockRequestQueryService.getMyUnblockRequests(
                user,
                cursor,
                size
        );
        ResponseDto<CursorPage<UnblockRequestResponse>> responseDto = HttpUtil.success(
                "Successfully retrieved my unblock requests",
                response
        );
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 차단 해제 요청 단일 조회
     */
    @GetMapping("/api/token/unblock-requests/{requestId}")
    public ResponseEntity<ResponseDto<UnblockRequestResponse>> getUnblockRequest(
            @CurrentUser User user,
            @PathVariable("requestId") Long requestId
    ) {
        UnblockRequestResponse response = unblockRequestQueryService.getUnblockRequest(user, requestId);
        ResponseDto<UnblockRequestResponse> responseDto = HttpUtil.success(
                "Successfully retrieved unblock request",
                response
        );
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 대기 중인 차단 해제 요청 목록 조회 (관리자 전용)
     */
    @GetMapping("/api/unblock-requests")
    public ResponseEntity<ResponseDto<CursorPage<UnblockRequestResponse>>> getPendingUnblockRequests(
            @CurrentUser User admin,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        CursorPage<UnblockRequestResponse> response = unblockRequestQueryService.getPendingUnblockRequests(
                admin,
                cursor,
                size
        );
        ResponseDto<CursorPage<UnblockRequestResponse>> responseDto = HttpUtil.success(
                "Successfully retrieved pending unblock requests",
                response
        );
        return ResponseEntity.ok(responseDto);
    }
}
