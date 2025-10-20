package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.key.ApiUsageId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiUsageRepository extends JpaRepository<ApiUsage, ApiUsageId> {

    Optional<ApiUsage> findByApiTokenAndEndpoint(ApiToken apiToken, String endpoint);
    
    @Query("SELECT au FROM ApiUsage au JOIN FETCH au.api a WHERE au.apiToken = :apiToken AND au.endpoint LIKE CONCAT(:endpointPrefix, '%') AND a.method = :method")
    List<ApiUsage> findCandidatesByPrefixAndMethod(
            @Param("apiToken") ApiToken apiToken, 
            @Param("endpointPrefix") String endpointPrefix, 
            @Param("method") String method
    );
    
    @Query("SELECT au FROM ApiUsage au " +
           "JOIN FETCH au.apiToken at " +
           "JOIN FETCH au.api a " +
           "JOIN FETCH au.apiUseReason aur " +
           "WHERE at.user.userId = :userId " +
           "AND au.id.apiTokenId < COALESCE(:cursor, 9223372036854775807) " +
           "ORDER BY au.id.apiTokenId DESC, au.id.apiId DESC")
    Slice<ApiUsage> findAllByUserIdWithCursor(
            @Param("userId") Long userId, 
            @Param("cursor") Long cursor, 
            Pageable pageable
    );

}

