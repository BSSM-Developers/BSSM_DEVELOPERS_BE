package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.dto.request.ApiHealthCheckRequest;
import com.example.bssm_dev.domain.api.dto.response.ApiHealthCheckResponse;
import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckApiService {
    public ApiHealthCheckResponse check(String endpoint, String method, String domain, HttpServletRequest httpServletRequest) {
        RequestInfo requestInfo = RequestInfo.of(httpServletRequest);
        Object response = ApiRequestExecutor.request(endpoint, method, domain, requestInfo);
        boolean healthy = response != null;
        return ApiHealthCheckResponse.of(healthy, response);
    }
}
