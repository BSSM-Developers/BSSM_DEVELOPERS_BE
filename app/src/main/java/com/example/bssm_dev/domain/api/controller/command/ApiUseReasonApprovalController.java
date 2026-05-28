package com.example.bssm_dev.domain.api.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.service.command.ApiUseReasonCommandService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/use-reason")
public class ApiUseReasonApprovalController {
    private final ApiUseReasonCommandService apiUseReasonCommandService;

    @PatchMapping("/{apiUseReasonId}/approve")
    public ResponseEntity<ResponseDto<Void>> approveApiUseReason(
            @PathVariable Long apiUseReasonId,
            @CurrentUser User user
    ) {
        apiUseReasonCommandService.approveApiUseReason(apiUseReasonId, user);
        return ResponseEntity.ok(HttpUtil.success("Successfully approved API use reason"));
    }

    @PatchMapping("/{apiUseReasonId}/reject")
    public ResponseEntity<ResponseDto<Void>> rejectApiUseReason(
            @PathVariable Long apiUseReasonId,
            @CurrentUser User user
    ) {
        apiUseReasonCommandService.rejectApiUseReason(apiUseReasonId, user);
        return ResponseEntity.ok(HttpUtil.success("Successfully rejected API use reason"));
    }
}
