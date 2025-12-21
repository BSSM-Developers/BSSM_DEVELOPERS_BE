package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.log.service.ProxyLogEventPublisher;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiTokenR2dbc;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiUsageR2dbc;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.service.query.ApiTokenReactiveQueryService;
import com.example.bssm_dev.domain.api.service.query.ApiUsageReactiveQueryService;
import com.example.bssm_dev.domain.api.service.query.TokenDomainReactiveQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class BrowserUseApiService {
    private final ApiTokenReactiveQueryService apiTokenReactiveQueryService;
    private final ApiUsageReactiveQueryService apiUsageReactiveQueryService;
    private final TokenDomainReactiveQueryService tokenDomainReactiveQueryService;
    private final ProxyLogEventPublisher proxyLogEventPublisher;

    public Mono<Object> get(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }


    public Mono<Object> post(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }

    public Mono<Object> patch(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }

    public Mono<Object> put(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }

    public Mono<Object> delete(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }

    private Mono<Object> handleRequest(String token, HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        RequestInfo requestInfo = RequestInfo.of(request);
        long startedAt = System.currentTimeMillis();

        return apiTokenReactiveQueryService.findByTokenClientId(token)
                .flatMap(apiToken ->
                    tokenDomainReactiveQueryService.findByApiTokenId(apiToken.getApiTokenId())
                            .collectList()
                            .doOnNext(tokenDomains -> apiToken.validateBrowserAccess(origin, tokenDomains))
                            .then(apiUsageReactiveQueryService.findByTokenAndEndpoint(apiToken, requestInfo))
                            .flatMap(apiUsage ->
                                ApiRequestExecutor.request(apiUsage, requestInfo)
                                        .doOnNext(response -> {
                                            proxyLogEventPublisher.publishSuccess(
                                                    ProxyLogDirection.BROWSER_TO_SERVER,
                                                    apiToken,
                                                    requestInfo,
                                                    request,
                                                    response,
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
                            )
                );
    }
}
