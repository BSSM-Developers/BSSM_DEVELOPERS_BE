package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.ApiUseReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUseReasonRepository extends JpaRepository<ApiUseReason, Long> {
}
