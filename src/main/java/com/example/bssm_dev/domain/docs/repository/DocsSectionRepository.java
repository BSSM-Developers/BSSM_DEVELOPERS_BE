package com.example.bssm_dev.domain.docs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocsSectionRepository extends JpaRepository<DocsSection, Long>, QuerydslPredicateExecutor<DocsSection> {
    @Query("SELECT COALESCE(MAX(ds.order), 0) FROM DocsSection ds WHERE ds.docs.docsId = :docsId")
    int findMaxOrderByDocsId(@Param("docsId") Long docsId);
}
