package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.dto.request.ApiHealthCheckRequest;
import com.example.bssm_dev.domain.api.dto.response.ApiHealthCheckResponse;
import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HealthCheckApiService {
    public Mono<ApiHealthCheckResponse> check(String endpoint, String method, String domain, HttpServletRequest httpServletRequest) {
        RequestInfo requestInfo = RequestInfo.of(httpServletRequest);
        return ApiRequestExecutor.request(endpoint, method, domain, requestInfo)
                .map(response -> {
                    boolean healthy = response != null;
                    return ApiHealthCheckResponse.of(healthy, response);
                })
                .onErrorResume(e -> Mono.just(ApiHealthCheckResponse.of(false, null)));
    }
}
