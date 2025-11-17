package com.example.bssm_dev.domain.api.controller.query;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenListResponse;
import com.example.bssm_dev.domain.api.service.query.ApiTokenQueryService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class ApiTokenQueryController {
    private final ApiTokenQueryService apiTokenQueryService;

    /**
     * 사용자의 API Token 목록 조회
     */
    @GetMapping
    public ResponseEntity<ResponseDto<CursorPage<ApiTokenListResponse>>> getApiTokenList(
            @CurrentUser User user,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        CursorPage<ApiTokenListResponse> response = apiTokenQueryService.getAllApiTokens(user, cursor, size);
        ResponseDto<CursorPage<ApiTokenListResponse>> responseDto = HttpUtil.success("Successfully retrieved API tokens", response);
        return ResponseEntity.ok(responseDto);
    }


    /**
     * 사용자의 API Token 단일 조회
     */
    @GetMapping("/{apiTokenId}")
    public ResponseEntity<ResponseDto<ApiTokenResponse>> getApiTokenDetail(
            @CurrentUser User user,
            @PathVariable Long apiTokenId
    ) {
        ApiTokenResponse response = apiTokenQueryService.getApiTokenDetail(user, apiTokenId);
        ResponseDto<ApiTokenResponse> responseDto = HttpUtil.success("Successfully retrieved API token detail", response);
        return ResponseEntity.ok(responseDto);
    }
}
