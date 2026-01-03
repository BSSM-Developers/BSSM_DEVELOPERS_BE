package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.UnblockRequest;
import com.example.bssm_dev.domain.api.model.type.UnblockRequestState;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnblockRequestRepository extends JpaRepository<UnblockRequest, Long> {
    
    Optional<UnblockRequest> findByApiTokenAndState(ApiToken apiToken, UnblockRequestState state);
    
    List<UnblockRequest> findByRequesterOrderByCreatedAtDesc(User requester);
    
    List<UnblockRequest> findByStateOrderByCreatedAtDesc(UnblockRequestState state);
    
    boolean existsByApiTokenAndState(ApiToken apiToken, UnblockRequestState state);
}
