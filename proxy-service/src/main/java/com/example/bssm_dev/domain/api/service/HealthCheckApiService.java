package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.common.util.DomainValidator;
import com.example.bssm_dev.domain.api.dto.response.ApiHealthCheckResponse;
import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HealthCheckApiService {
    private final ApiRequestExecutor apiRequestExecutor;

    public Mono<ApiHealthCheckResponse> check(String endpoint, String method, String domain, ServerHttpRequest request, byte[] body) {
        DomainValidator.validate(domain);
        RequestInfo requestInfo = RequestInfo.of(request, body);
        return apiRequestExecutor.request(endpoint, method, domain, requestInfo)
                .map(response -> {
                    boolean healthy = response != null && !response.getStatusCode().isError();
                    byte[] responseBody = response != null ? response.getBody() : null;
                    return ApiHealthCheckResponse.of(healthy, responseBody);
                })
                .onErrorResume(e -> Mono.just(ApiHealthCheckResponse.of(false, null)));
    }
}
