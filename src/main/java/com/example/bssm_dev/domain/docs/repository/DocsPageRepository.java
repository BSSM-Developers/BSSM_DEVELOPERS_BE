package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.DocsPage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocsPageRepository extends MongoRepository<DocsPage, String> {
    Optional<DocsPage> findByDocsIdAndMappedId(String docsId, String mappedId);

    void deleteByDocsId(String docsId);
}
