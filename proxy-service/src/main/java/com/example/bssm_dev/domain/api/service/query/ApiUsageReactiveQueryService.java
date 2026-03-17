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
        String actualPath = requestInfo.endpoint().split("\\?")[0];
        String cacheKey = CacheKeys.apiUsageList(apiToken.getApiTokenId());
        return cacheService.getCacheOrFetchList(
                cacheKey,
                () -> apiUsageR2dbcRepository.findAllByApiTokenId(apiToken.getApiTokenId()),
                ApiUsageR2dbc.class
        )
        .filter(usage -> matchesTemplate(usage.getEndpoint(), actualPath))
        .next()
        .switchIfEmpty(Mono.error(ApiUsageNotFoundException::raise));
    }

    private boolean matchesTemplate(String template, String actualPath) {
        String regex = template.replaceAll("\\{[^}]+}", "[^/?]+");
        return actualPath.matches(regex);
    }
}
