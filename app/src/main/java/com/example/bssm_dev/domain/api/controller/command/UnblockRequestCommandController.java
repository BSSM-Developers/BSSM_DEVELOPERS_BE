package com.example.bssm_dev.domain.api.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.request.CreateUnblockRequestRequest;
import com.example.bssm_dev.domain.api.dto.request.RejectUnblockRequestRequest;
import com.example.bssm_dev.domain.api.dto.response.UnblockRequestResponse;
import com.example.bssm_dev.domain.api.service.command.UnblockRequestCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UnblockRequestCommandController {
    private final UnblockRequestCommandService unblockRequestCommandService;

    /**
     * API Token 차단 해제 요청 생성
     */
    @PostMapping("/api/token/{tokenId}/unblock-requests")
    public ResponseEntity<ResponseDto<Void>> createUnblockRequest(
            @CurrentUser User user,
            @PathVariable("tokenId") Long tokenId,
            @Valid @RequestBody CreateUnblockRequestRequest request
    ) {
        unblockRequestCommandService.createUnblockRequest(
                user,
                tokenId,
                request.reason()
        );
        ResponseDto<Void> responseDto = HttpUtil.success(
                "Successfully created unblock request"
        );
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 차단 해제 요청 승인 (관리자 전용)
     */
    @PostMapping("/api/unblock-requests/{requestId}/approve")
    public ResponseEntity<ResponseDto<Void>> approveUnblockRequest(
            @CurrentUser User admin,
            @PathVariable("requestId") Long requestId
    ) {
        unblockRequestCommandService.approveUnblockRequest(
                admin,
                requestId
        );
        ResponseDto<Void> responseDto = HttpUtil.success(
                "Successfully approved unblock request"
        );
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 차단 해제 요청 거부 (관리자 전용)
     */
    @PostMapping("/api/unblock-requests/{requestId}/reject")
    public ResponseEntity<ResponseDto<Void>> rejectUnblockRequest(
            @CurrentUser User admin,
            @PathVariable("requestId") Long requestId,
            @Valid @RequestBody RejectUnblockRequestRequest request
    ) {
        unblockRequestCommandService.rejectUnblockRequest(
                admin,
                requestId,
                request.rejectReason()
        );
        ResponseDto<Void> responseDto = HttpUtil.success(
                "Successfully rejected unblock request"
        );
        return ResponseEntity.ok(responseDto);
    }
}
