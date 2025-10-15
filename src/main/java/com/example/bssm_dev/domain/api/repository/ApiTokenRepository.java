package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.ApiToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiTokenRepository extends JpaRepository<ApiToken, Long> {
    Optional<ApiToken> findByUser_UserId(Long userId);
    
    @Query("SELECT at FROM ApiToken at WHERE at.user.userId = :userId AND at.apiTokenId < COALESCE(:cursor, 9223372036854775807) ORDER BY at.apiTokenId DESC")
    Slice<ApiToken> findAllByUserIdWithCursorOrderByApiTokenIdDesc(@Param("userId") Long userId, @Param("cursor") Long cursor, Pageable pageable);
}
