package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<Api, Long>, QuerydslPredicateExecutor<Api> {
}
