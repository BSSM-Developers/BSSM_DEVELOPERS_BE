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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/use-reason")
public class ApiUseReasonQueryController {
    private final ApiUseReasonQueryService apiUseReasonQueryService;

    @GetMapping
    public ResponseEntity<ResponseDto<CursorPage<ApiUseReasonResponse>>> getApiUseReasonList(
            @CurrentUser User user,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        CursorPage<ApiUseReasonResponse> response = apiUseReasonQueryService.getAllApiUseReasons(user, cursor, size);
        ResponseDto<CursorPage<ApiUseReasonResponse>> responseDto = HttpUtil.success("Successfully retrieved API use reasons", response);
        return ResponseEntity.ok(responseDto);
    }
}
