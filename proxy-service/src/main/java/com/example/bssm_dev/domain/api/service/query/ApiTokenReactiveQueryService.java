package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiTokenR2dbc;
import com.example.bssm_dev.domain.api.repository.r2dbc.ApiTokenR2dbcRepository;
import com.example.bssm_dev.proxy.cache.CacheKeys;
import com.example.bssm_dev.proxy.cache.ReactiveCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApiTokenReactiveQueryService {
    private final ApiTokenR2dbcRepository apiTokenR2dbcRepository;
    private final ReactiveCacheService cacheService;

    public Mono<ApiTokenR2dbc> findByTokenClientId(String clientId) {
        String cacheKey = CacheKeys.apiToken(clientId);
        return cacheService.getCacheOrFetch(
                cacheKey,
                () -> apiTokenR2dbcRepository.findByClientId(clientId),
                ApiTokenR2dbc.class
        ).switchIfEmpty(Mono.error(ApiTokenNotFoundException::raise));
    }
}
