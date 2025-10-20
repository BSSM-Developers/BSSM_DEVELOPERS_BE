package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.dto.request.ApiHealthCheckRequest;
import com.example.bssm_dev.domain.api.dto.response.ApiHealthCheckResponse;
import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckApiService {
    public ApiHealthCheckResponse check(ApiHealthCheckRequest apiHealthCheckRequest) {
        Object response = ApiRequestExecutor.request(apiHealthCheckRequest);
        boolean healthy = response != null;
        return ApiHealthCheckResponse.of(healthy, response);
    }
}
