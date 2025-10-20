package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.ApiUseReason;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUseReasonRepository extends JpaRepository<ApiUseReason, Long> {
    
    @Query("SELECT aur FROM ApiUseReason aur " +
           "JOIN FETCH aur.writer w " +
           "JOIN FETCH aur.api a " +
           "JOIN FETCH aur.apiToken at " +
           "WHERE w.userId = :userId " +
           "AND aur.apiUseReasonId < COALESCE(:cursor, 9223372036854775807) " +
           "ORDER BY aur.apiUseReasonId DESC")
    Slice<ApiUseReason> findAllByUserIdWithCursor(
            @Param("userId") Long userId, 
            @Param("cursor") Long cursor, 
            Pageable pageable
    );
}

