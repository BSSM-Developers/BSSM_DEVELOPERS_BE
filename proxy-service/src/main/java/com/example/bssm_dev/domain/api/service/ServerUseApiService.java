package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ServerUseApiService {
    private final PasswordEncoder passwordEncoder;
    private final ApiProxyPipeline pipeline;

    public Mono<ResponseEntity<byte[]>> handle(String secretKey, String token, ServerHttpRequest request, byte[] body) {
        RequestInfo requestInfo = RequestInfo.of(request, body);
        return pipeline.execute(
                token, requestInfo, request,
                apiToken -> Mono.fromCallable(() -> {
                    apiToken.validateServerAccess(secretKey, passwordEncoder);
                    return null;
                }).subscribeOn(Schedulers.boundedElastic()).then(),
                ProxyLogDirection.SERVER_TO_SERVER
        );
    }
}
