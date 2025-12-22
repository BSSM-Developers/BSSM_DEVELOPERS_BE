package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.model.r2dbc.TokenDomainR2dbc;
import com.example.bssm_dev.domain.api.repository.r2dbc.TokenDomainR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenDomainReactiveQueryService {
    private final TokenDomainR2dbcRepository tokenDomainR2dbcRepository;

    public Flux<TokenDomainR2dbc> findByApiTokenId(Long apiTokenId) {
        return tokenDomainR2dbcRepository.findByApiTokenId(apiTokenId);
    }
}
