package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.exception.ApiUsageNotFoundException;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiTokenR2dbc;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiUsageR2dbc;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.repository.r2dbc.ApiUsageR2dbcRepository;
import com.example.bssm_dev.proxy.cache.CacheKeys;
import com.example.bssm_dev.proxy.cache.ReactiveCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApiUsageReactiveQueryService {
    private final ApiUsageR2dbcRepository apiUsageR2dbcRepository;
    private final ReactiveCacheService cacheService;

    public Mono<ApiUsageR2dbc> findByTokenAndEndpoint(ApiTokenR2dbc apiToken, RequestInfo requestInfo) {
        String endpoint = requestInfo.endpoint();
        String cacheKey = CacheKeys.apiUsage(apiToken.getApiTokenId(), endpoint);
        return cacheService.getCacheOrFetch(
                cacheKey,
                () -> apiUsageR2dbcRepository.findByApiTokenIdAndEndpoint(apiToken.getApiTokenId(), endpoint),
                ApiUsageR2dbc.class
        ).switchIfEmpty(Mono.error(ApiUsageNotFoundException::raise));
    }
}
