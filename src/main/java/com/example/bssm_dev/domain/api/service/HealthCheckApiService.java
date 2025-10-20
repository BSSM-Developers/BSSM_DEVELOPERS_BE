package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.dto.response.ApiHealthCheckResponse;
import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.service.query.ApiQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckApiService {
    private final ApiQueryService apiQueryService;

    public ApiHealthCheckResponse check(Long apiId, Object body) {
        Api api = apiQueryService.findById(apiId);
        Object response = ApiRequestExecutor.request(api, body);
        boolean healthy = response != null;
        return ApiHealthCheckResponse.of(healthy);
    }
}
