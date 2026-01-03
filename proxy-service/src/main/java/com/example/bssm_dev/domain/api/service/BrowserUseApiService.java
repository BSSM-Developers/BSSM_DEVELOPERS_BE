package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.event.ApiRequestCompletedEvent;
import com.example.bssm_dev.domain.api.event.ApiRequestStartedEvent;
import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.log.service.ProxyLogEventPublisher;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiTokenR2dbc;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.service.query.ApiTokenReactiveQueryService;
import com.example.bssm_dev.domain.api.service.query.ApiUsageReactiveQueryService;
import com.example.bssm_dev.domain.api.service.query.TokenDomainReactiveQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class BrowserUseApiService {
    private final ApiTokenReactiveQueryService apiTokenReactiveQueryService;
    private final ApiUsageReactiveQueryService apiUsageReactiveQueryService;
    private final TokenDomainReactiveQueryService tokenDomainReactiveQueryService;
    private final ProxyLogEventPublisher proxyLogEventPublisher;
    private final ApplicationEventPublisher eventPublisher;

    public Mono<ResponseEntity<byte[]>> get(String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(token, request, body);
    }


    public Mono<ResponseEntity<byte[]>> post(String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(token, request, body);
    }

    public Mono<ResponseEntity<byte[]>> patch(String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(token, request, body);
    }

    public Mono<ResponseEntity<byte[]>> put(String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(token, request, body);
    }

    public Mono<ResponseEntity<byte[]>> delete(String token, ServerHttpRequest request, byte[] body) {
        return handleRequest(token, request, body);
    }

    private Mono<ResponseEntity<byte[]>> handleRequest(String token, ServerHttpRequest request, byte[] body) {
        String origin = request.getHeaders().getOrigin();
        RequestInfo requestInfo = RequestInfo.of(request, body);
        long startedAt = System.currentTimeMillis();

        return apiTokenReactiveQueryService.findByTokenClientId(token)
                .flatMap(apiToken ->
                    tokenDomainReactiveQueryService.findByApiTokenId(apiToken.getApiTokenId())
                            .collectList()
                            .doOnNext(tokenDomains -> {
                                apiToken.validateBrowserAccess(origin, tokenDomains);
                                apiToken.validateNotBlocked();
                                eventPublisher.publishEvent(new ApiRequestStartedEvent(apiToken.getApiTokenId()));
                            })
                            .then(apiUsageReactiveQueryService.findByTokenAndEndpoint(apiToken, requestInfo))
                            .flatMap(apiUsage ->
                                ApiRequestExecutor.request(apiUsage, requestInfo)
                                        .doOnNext(response -> {
                                            proxyLogEventPublisher.publishSuccess(
                                                    ProxyLogDirection.BROWSER_TO_SERVER,
                                                    apiToken,
                                                    requestInfo,
                                                    request,
                                                    response.getBody(),
                                                    startedAt
                                            );
                                        })
                                        .doOnError(e -> {
                                            proxyLogEventPublisher.publishError(
                                                    ProxyLogDirection.BROWSER_TO_SERVER,
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
