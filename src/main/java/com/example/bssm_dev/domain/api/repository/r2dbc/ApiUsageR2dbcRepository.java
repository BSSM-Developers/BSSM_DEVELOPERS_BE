package com.example.bssm_dev.domain.api.repository.r2dbc;

import com.example.bssm_dev.domain.api.model.r2dbc.ApiUsageR2dbc;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ApiUsageR2dbcRepository extends R2dbcRepository<ApiUsageR2dbc, Long> {

    @Query("""
        SELECT au.*, a.domain, a.method
        FROM api_usage au
        INNER JOIN api a ON au.api_id = a.api_id
        WHERE au.api_token_id = :apiTokenId
        AND au.endpoint = :endpoint
        LIMIT 1
    """)
    Mono<ApiUsageR2dbc> findByApiTokenIdAndEndpoint(Long apiTokenId, String endpoint);
}
