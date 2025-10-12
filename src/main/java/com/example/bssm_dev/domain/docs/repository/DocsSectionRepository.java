package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.DocsSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocsSectionRepository extends JpaRepository<DocsSection, Long>, QuerydslPredicateExecutor<DocsSection> {
}
