package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.ApiPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiPageRepository extends JpaRepository<ApiPage, Long>, QuerydslPredicateExecutor<ApiPage> {
}
