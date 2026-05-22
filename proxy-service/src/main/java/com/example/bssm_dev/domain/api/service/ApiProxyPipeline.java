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
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * 브라우저/서버 프록시 요청의 공통 실행 파이프라인.
 * 토큰 조회 → 접근 검증 → 사용량 조회 → 외부 API 호출 → 로그 발행 순서를 정의하며,
 * 접근 검증 전략(browser/server)은 호출자가 주입한다.
 */
@Component
@RequiredArgsConstructor
public class ApiProxyPipeline {
    private final ApiTokenReactiveQueryService apiTokenReactiveQueryService;
    private final ApiUsageReactiveQueryService apiUsageReactiveQueryService;
    private final ApiRequestExecutor apiRequestExecutor;
    private final ProxyLogEventPublisher proxyLogEventPublisher;
    private final ApplicationEventPublisher eventPublisher;

    public Mono<ResponseEntity<byte[]>> execute(
            String clientId,
            RequestInfo requestInfo,
            ServerHttpRequest httpRequest,
            Function<ApiTokenR2dbc, Mono<Void>> tokenValidator,
            ProxyLogDirection direction
    ) {
        long startedAt = System.currentTimeMillis();
        return apiTokenReactiveQueryService.findByTokenClientId(clientId)
                .flatMap(apiToken -> tokenValidator.apply(apiToken).thenReturn(apiToken))
                .doOnNext(apiToken -> {
                    apiToken.validateNotBlocked();
                    eventPublisher.publishEvent(new ApiRequestStartedEvent(apiToken.getApiTokenId()));
                })
                .flatMap(apiToken ->
                        apiUsageReactiveQueryService.findByTokenAndEndpoint(apiToken, requestInfo)
                                .flatMap(apiUsage ->
                                        apiRequestExecutor.request(apiUsage, requestInfo)
                                                .doOnNext(response -> proxyLogEventPublisher.publishSuccess(
                                                        direction, apiToken, requestInfo, httpRequest,
                                                        response.getBody(), startedAt))
                                                .doOnError(e -> proxyLogEventPublisher.publishError(
                                                        direction, apiToken, requestInfo, httpRequest, e, startedAt))
                                                .doFinally(signal -> eventPublisher.publishEvent(
                                                        new ApiRequestCompletedEvent(apiToken.getApiTokenId())))
                                )
                );
    }
}
