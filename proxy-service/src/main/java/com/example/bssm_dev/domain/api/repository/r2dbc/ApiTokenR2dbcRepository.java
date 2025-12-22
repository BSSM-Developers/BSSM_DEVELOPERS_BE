package com.example.bssm_dev.domain.api.repository.r2dbc;

import com.example.bssm_dev.domain.api.model.r2dbc.ApiTokenR2dbc;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ApiTokenR2dbcRepository extends R2dbcRepository<ApiTokenR2dbc, Long> {

    @Query("SELECT * FROM api_token WHERE api_token_uuid = :clientId")
    Mono<ApiTokenR2dbc> findByClientId(String clientId);
}
