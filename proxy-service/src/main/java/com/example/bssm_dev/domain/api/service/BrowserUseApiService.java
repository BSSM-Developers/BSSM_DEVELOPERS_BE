package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.service.query.TokenDomainReactiveQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BrowserUseApiService {
    private final TokenDomainReactiveQueryService tokenDomainReactiveQueryService;
    private final ApiProxyPipeline pipeline;

    public Mono<ResponseEntity<byte[]>> handle(String token, ServerHttpRequest request, byte[] body) {
        String origin = request.getHeaders().getOrigin();
        RequestInfo requestInfo = RequestInfo.of(request, body);
        return pipeline.execute(
                token, requestInfo, request,
                apiToken -> tokenDomainReactiveQueryService.findByApiTokenId(apiToken.getApiTokenId())
                        .collectList()
                        .doOnNext(domains -> apiToken.validateBrowserAccess(origin, domains))
                        .then(),
                ProxyLogDirection.BROWSER_TO_SERVER
        );
    }
}
