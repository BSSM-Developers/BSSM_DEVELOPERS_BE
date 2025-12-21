package com.example.bssm_dev.domain.api.repository.r2dbc;

import com.example.bssm_dev.domain.api.model.r2dbc.TokenDomainR2dbc;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TokenDomainR2dbcRepository extends R2dbcRepository<TokenDomainR2dbc, Long> {

    @Query("SELECT * FROM token_domain WHERE api_token_id = :apiTokenId")
    Flux<TokenDomainR2dbc> findByApiTokenId(Long apiTokenId);
}
