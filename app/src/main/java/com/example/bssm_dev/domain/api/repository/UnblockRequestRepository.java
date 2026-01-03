package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.UnblockRequest;
import com.example.bssm_dev.domain.api.model.type.UnblockRequestState;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UnblockRequestRepository extends JpaRepository<UnblockRequest, Long> {
    
    Optional<UnblockRequest> findByApiTokenAndState(ApiToken apiToken, UnblockRequestState state);
    
    List<UnblockRequest> findByRequesterOrderByCreatedAtDesc(User requester);
    
    List<UnblockRequest> findByStateOrderByCreatedAtDesc(UnblockRequestState state);

    @Query("SELECT ur FROM UnblockRequest ur " +
           "JOIN FETCH ur.apiToken at " +
           "JOIN FETCH ur.requester r " +
           "LEFT JOIN FETCH ur.reviewer rv " +
           "WHERE ur.state = :state " +
           "AND ur.unblockRequestId < COALESCE(:cursor, 9223372036854775807) " +
           "ORDER BY ur.unblockRequestId DESC")
    Slice<UnblockRequest> findByStateWithCursor(
            @Param("state") UnblockRequestState state,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @Query("SELECT ur FROM UnblockRequest ur " +
           "JOIN FETCH ur.apiToken at " +
           "JOIN FETCH ur.requester r " +
           "LEFT JOIN FETCH ur.reviewer rv " +
           "WHERE r = :requester " +
           "AND ur.unblockRequestId < COALESCE(:cursor, 9223372036854775807) " +
           "ORDER BY ur.unblockRequestId DESC")
    Slice<UnblockRequest> findByRequesterWithCursor(
            @Param("requester") User requester,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
    
    boolean existsByApiTokenAndState(ApiToken apiToken, UnblockRequestState state);
}
