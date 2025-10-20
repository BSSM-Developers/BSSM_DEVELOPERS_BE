package com.example.bssm_dev.domain.api.controller;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.request.ApiHealthCheckRequest;
import com.example.bssm_dev.domain.api.dto.response.ApiHealthCheckResponse;
import com.example.bssm_dev.domain.api.service.HealthCheckApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/healthy")
public class HealthCheckApiController {
    private final HealthCheckApiService healthCheckApiService;

    /**
     * API 헬스체크 with Endpoint, Method
     */
    @PostMapping
    public ResponseEntity<ResponseDto<ApiHealthCheckResponse>> healthCheckWithApiId(
            @RequestBody(required = false) ApiHealthCheckRequest apiHealthCheckRequest
    ) {
        ApiHealthCheckResponse healthCheckResponse = healthCheckApiService.check(apiHealthCheckRequest);


        ResponseDto<ApiHealthCheckResponse> responseDto = HttpUtil.success("heath check ok", healthCheckResponse);
        return ResponseEntity.ok(responseDto);
    }
}
