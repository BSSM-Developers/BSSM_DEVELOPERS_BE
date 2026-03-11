package com.example.bssm_dev.domain.api.controller.query;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.response.ApiUseReasonResponse;
import com.example.bssm_dev.domain.api.service.query.ApiUseReasonQueryService;
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
@RequestMapping("/api/use-reason")
public class ApiUseReasonQueryController {
    private final ApiUseReasonQueryService apiUseReasonQueryService;

    /**
     * 본인의 API Use Reason 목록 조회
     */
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<CursorPage<ApiUseReasonResponse>>> getMyApiUseReasonList(
            @CurrentUser User user,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        CursorPage<ApiUseReasonResponse> response = apiUseReasonQueryService.getAllApiUseReasons(user, cursor, size);
        ResponseDto<CursorPage<ApiUseReasonResponse>> responseDto = HttpUtil.success("Successfully retrieved API use reasons", response);
        return ResponseEntity.ok(responseDto);
    }
    
    /**
     * 본인이 등록한 API에 대한 사용 신청 목록 조회
     */
    @GetMapping("/by-api/{apiId}")
    public ResponseEntity<ResponseDto<CursorPage<ApiUseReasonResponse>>> getApiUseReasonsByApiId(
            @CurrentUser User user,
            @PathVariable String apiId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        CursorPage<ApiUseReasonResponse> response = apiUseReasonQueryService.getApiUseReasonsByApiId(user, apiId, cursor, size);
        ResponseDto<CursorPage<ApiUseReasonResponse>> responseDto = HttpUtil.success("Successfully retrieved API use reasons", response);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 어드민 - 모든 API Use Reason 조회 (상태별 필터링)
     */
    @GetMapping
    public ResponseEntity<ResponseDto<CursorPage<ApiUseReasonResponse>>> getApiUseReasonsByState(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String state
    ) {
        CursorPage<ApiUseReasonResponse> response = apiUseReasonQueryService.getApiUseReasonsByState(cursor, size, state);
        ResponseDto<CursorPage<ApiUseReasonResponse>> responseDto = HttpUtil.success("Successfully retrieved API use reasons", response);
        return ResponseEntity.ok(responseDto);
    }
}

