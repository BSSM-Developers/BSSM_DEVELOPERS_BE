package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.type.DocsType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocsRepository extends JpaRepository<Docs, Long>, QuerydslPredicateExecutor<Docs> {

    @EntityGraph(attributePaths={"creator"})
    @Query("SELECT d FROM Docs d WHERE (:type IS NULL OR d.type = :type) AND d.docsId < COALESCE(:cursor, 9223372036854775807) ORDER BY d.docsId DESC")
    Slice<Docs> findAllWithCursorOrderByDocsIdDesc(@Param("type") DocsType type, @Param("cursor") Long cursor, Pageable pageable);

    @EntityGraph(attributePaths={"creator"})
    @Query("SELECT d FROM Docs d WHERE d.creator.userId = :userId AND (:type IS NULL OR d.type = :type) AND d.docsId < COALESCE(:cursor, 9223372036854775807) ORDER BY d.docsId DESC")
    Slice<Docs> findMyDocsWithCursorOrderByDocsIdDesc(@Param("userId") Long userId, @Param("type") DocsType type, @Param("cursor") Long cursor, Pageable pageable);
}
