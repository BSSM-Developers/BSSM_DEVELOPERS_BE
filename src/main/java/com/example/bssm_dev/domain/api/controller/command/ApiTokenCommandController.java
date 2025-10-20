package com.example.bssm_dev.domain.api.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.request.ChangeApiTokenNameRequest;
import com.example.bssm_dev.domain.api.dto.request.CreateApiTokenRequest;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.mapper.ApiTokenMapper;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.service.command.ApiTokenCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class ApiTokenCommandController {
    private final ApiTokenCommandService apiTokenCommandService;

    /**
     * API Token 생성
     */
    @PostMapping
    public ResponseEntity<ResponseDto<ApiTokenResponse>> createApiToken(
            @CurrentUser User user,
            @Valid @RequestBody CreateApiTokenRequest request
    ) {
        ApiTokenResponse apiTokenResponse = apiTokenCommandService.createApiToken(user, request.apiTokenName());
        ResponseDto<ApiTokenResponse> responseDto = HttpUtil.success("Successfully created API token", apiTokenResponse);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * API Token Secret Key 재발급
     */
    @PatchMapping("/{tokenId}/secret")
    public ResponseEntity<ResponseDto<ApiTokenResponse>> reGenerateSecretKeyOfApiToken(
            @CurrentUser User user,
            @PathVariable("tokenId") Long tokenId
    ) {
        ApiTokenResponse apiTokenResponse = apiTokenCommandService.reGenerateSecretKey(user, tokenId);
        ResponseDto<ApiTokenResponse> responseDto = HttpUtil.success("Successfully regenerated API token secret key", apiTokenResponse);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * API Token 이름 변경
     */
    @PatchMapping("/{tokenId}/name")
    public ResponseEntity<ResponseDto<ApiTokenResponse>> changeApiTokenName(
            @CurrentUser User user,
            @PathVariable("tokenId") Long tokenId,
            @Valid @RequestBody ChangeApiTokenNameRequest request
    ) {
        ApiTokenResponse apiTokenResponse = apiTokenCommandService.changeApiTokenName(user, tokenId, request.apiTokenName());
        ResponseDto<ApiTokenResponse> responseDto = HttpUtil.success("Successfully changed API token name", apiTokenResponse);
        return ResponseEntity.ok(responseDto);
    }
}
