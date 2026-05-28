package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.DocsPage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocsPageRepository extends MongoRepository<DocsPage, String> {
    Optional<DocsPage> findByDocsIdAndMappedId(String docsId, String mappedId);

    List<DocsPage> findAllByDocsId(String docsId);

    @Query("{ 'endpoint': ?0 }")
    Optional<DocsPage> findByEndpoint(String endpoint);

    @Query(value = "{ 'docsId': ?0, 'sourceDocsId': ?1, 'sourceMappedId': ?2 }", exists = true)
    boolean existsByDocsIdAndSourceDocsIdAndSourceMappedId(String docsId, String sourceDocsId, String sourceMappedId);

    void deleteByDocsId(String docsId);
}
