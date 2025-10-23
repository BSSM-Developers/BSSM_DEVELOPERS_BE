package com.example.bssm_dev.domain.api.controller.query;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.response.ApiUsageResponse;
import com.example.bssm_dev.domain.api.service.query.ApiUsageQueryService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class ApiUsageQueryController {
    private final ApiUsageQueryService apiUsageQueryService;

    /**
     * 사용자의 API Usage 목록 조회
     */
    @GetMapping
    public ResponseEntity<ResponseDto<CursorPage<ApiUsageResponse>>> getApiUsageList(
            @CurrentUser User user,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        CursorPage<ApiUsageResponse> response = apiUsageQueryService.getAllApiUsages(user, cursor, size);
        ResponseDto<CursorPage<ApiUsageResponse>> responseDto = HttpUtil.success("Successfully retrieved API usages", response);
        return ResponseEntity.ok(responseDto);
    }
}
