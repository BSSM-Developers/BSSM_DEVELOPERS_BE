package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.log.service.ProxyLogEventPublisher;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiTokenR2dbc;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiUsageR2dbc;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.service.query.ApiTokenReactiveQueryService;
import com.example.bssm_dev.domain.api.service.query.ApiUsageReactiveQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class ServerUseApiService {
    private final ApiTokenReactiveQueryService apiTokenReactiveQueryService;
    private final ApiUsageReactiveQueryService apiUsageReactiveQueryService;
    private final PasswordEncoder passwordEncoder;
    private final ProxyLogEventPublisher proxyLogEventPublisher;

    public Mono<ResponseEntity<byte[]>> get(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Mono<ResponseEntity<byte[]>> post(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Mono<ResponseEntity<byte[]>> patch(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Mono<ResponseEntity<byte[]>> put(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Mono<ResponseEntity<byte[]>> delete(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    private Mono<ResponseEntity<byte[]>> handleRequest(String secretKey, String token, HttpServletRequest request) {
        RequestInfo requestInfo = RequestInfo.of(request);
        long startedAt = System.currentTimeMillis();

        return apiTokenReactiveQueryService.findByTokenClientId(token)
                .doOnNext(apiToken -> apiToken.validateServerAccess(secretKey, passwordEncoder))
                .flatMap(apiToken ->
                    apiUsageReactiveQueryService.findByTokenAndEndpoint(apiToken, requestInfo)
                            .flatMap(apiUsage ->
                                ApiRequestExecutor.request(apiUsage, requestInfo)
                                        .doOnNext(response -> {
                                            proxyLogEventPublisher.publishSuccess(
                                                    ProxyLogDirection.SERVER_TO_SERVER,
                                                    apiToken,
                                                    requestInfo,
                                                    request,
                                                    response.getBody(),
                                                    startedAt
                                            );
                                        })
                                        .doOnError(e -> {
                                            proxyLogEventPublisher.publishError(
                                                    ProxyLogDirection.SERVER_TO_SERVER,
                                                    apiToken,
                                                    requestInfo,
                                                    request,
                                                    e,
                                                    startedAt
                                            );
                                        })
                            )
                );
    }
}
