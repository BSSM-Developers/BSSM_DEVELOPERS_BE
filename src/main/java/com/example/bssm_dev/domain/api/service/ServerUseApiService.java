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

    public Mono<Object> get(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Mono<Object> post(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Mono<Object> patch(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Mono<Object> put(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Mono<Object> delete(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    private Mono<Object> handleRequest(String secretKey, String token, HttpServletRequest request) {
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
                                                    response,
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
