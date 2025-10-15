package com.example.bssm_dev.domain.api.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.service.command.ApiTokenCommandService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class ApiTokenCommandController {
    private final ApiTokenCommandService apiTokenCommandService;

    @PostMapping
    public ResponseEntity<ResponseDto<ApiTokenResponse>> createApiToken(
            @CurrentUser User user
    ) {
        ApiTokenResponse apiTokenResponse = apiTokenCommandService.createApiToken(user);
        ResponseDto<ApiTokenResponse> responseDto = HttpUtil.success("Successfully created API token", apiTokenResponse);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/api/token/{tokenId}/secret")
    public ResponseEntity<ResponseDto<ApiTokenResponse>> updateApiToken(
            @CurrentUser User user,
            @PathVariable("tokenId") Long tokenId
    ) {
        ApiTokenResponse apiTokenResponse = apiTokenCommandService.reGenerateSecretKey(user, tokenId);
        ResponseDto<ApiTokenResponse> responseDto = HttpUtil.success("Successfully created API token", apiTokenResponse);
        return ResponseEntity.ok(responseDto);
    }
}
