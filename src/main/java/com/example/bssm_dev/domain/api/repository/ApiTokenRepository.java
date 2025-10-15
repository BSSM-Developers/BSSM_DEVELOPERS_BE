package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.ApiToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiTokenRepository extends JpaRepository<ApiToken, Long> {
    Optional<ApiToken> findByUser_UserId(Long userId);
}
