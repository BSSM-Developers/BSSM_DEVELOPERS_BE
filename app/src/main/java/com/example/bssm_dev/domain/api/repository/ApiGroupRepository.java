package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.ApiGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiGroupRepository extends JpaRepository<ApiGroup, String> {
}
