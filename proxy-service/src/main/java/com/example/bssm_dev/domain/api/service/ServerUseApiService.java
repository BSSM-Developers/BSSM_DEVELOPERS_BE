package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.event.ApiRequestCompletedEvent;
import com.example.bssm_dev.domain.api.event.ApiRequestStartedEvent;
import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.log.service.ProxyLogEventPublisher;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.service.query.ApiTokenReactiveQueryService;
import com.example.bssm_dev.domain.api.service.query.ApiUsageReactiveQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
    private final ApplicationEventPublisher eventPublisher;

    public Mono<ResponseEntity<byte[]>> get(String secretKey, String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(secretKey, token, request, body);
    }

    public Mono<ResponseEntity<byte[]>> post(String secretKey, String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(secretKey, token, request, body);
    }

    public Mono<ResponseEntity<byte[]>> patch(String secretKey, String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(secretKey, token, request, body);
    }

    public Mono<ResponseEntity<byte[]>> put(String secretKey, String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(secretKey, token, request, body);
    }

    public Mono<ResponseEntity<byte[]>> delete(String secretKey, String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(secretKey, token, request, body);
    }

    private Mono<ResponseEntity<byte[]>> handleRequest(String secretKey, String token, ServerHttpRequest request, byte[] body) {
        RequestInfo requestInfo = RequestInfo.of(request, body);
        long startedAt = System.currentTimeMillis();

        return apiTokenReactiveQueryService.findByTokenClientId(token)
                .doOnNext(apiToken -> {
                    apiToken.validateServerAccess(secretKey, passwordEncoder);
                    apiToken.validateNotBlocked();
                    eventPublisher.publishEvent(new ApiRequestStartedEvent(apiToken.getApiTokenId()));
                })
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
                                        .doFinally(signalType -> {
                                            eventPublisher.publishEvent(new ApiRequestCompletedEvent(apiToken.getApiTokenId()));
                                        })
                            )
                );
    }
}
