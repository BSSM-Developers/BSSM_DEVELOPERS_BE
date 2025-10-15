package com.example.bssm_dev.domain.api.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.request.CreateApiUseReasonRequest;
import com.example.bssm_dev.domain.api.service.command.ApiUseReasonCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{apiTokenId}/use-reason")
public class ApiUseReasonCommandController {
    private final ApiUseReasonCommandService apiUseReasonCommandService;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createApiUseReason(
            @Valid @RequestBody CreateApiUseReasonRequest request,
            @CurrentUser User user,
            @PathVariable("apiTokenId") Long apiTokenId
    ) {
         apiUseReasonCommandService.createApiUseReason(request, user, apiTokenId);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully created API use reason");
        return ResponseEntity.ok(responseDto);
    }
}
