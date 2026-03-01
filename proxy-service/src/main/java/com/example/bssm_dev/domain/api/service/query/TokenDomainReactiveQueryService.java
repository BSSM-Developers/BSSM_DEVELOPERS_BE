package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.model.r2dbc.TokenDomainR2dbc;
import com.example.bssm_dev.domain.api.repository.r2dbc.TokenDomainR2dbcRepository;
import com.example.bssm_dev.proxy.cache.CacheKeys;
import com.example.bssm_dev.proxy.cache.ReactiveCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class TokenDomainReactiveQueryService {
    private final TokenDomainR2dbcRepository tokenDomainR2dbcRepository;
    private final ReactiveCacheService cacheService;

    public Flux<TokenDomainR2dbc> findByApiTokenId(Long apiTokenId) {
        String cacheKey = CacheKeys.tokenDomain(apiTokenId);
        return cacheService.getCacheOrFetchList(
                cacheKey,
                () -> tokenDomainR2dbcRepository.findByApiTokenId(apiTokenId),
                TokenDomainR2dbc.class
        );
    }
}
