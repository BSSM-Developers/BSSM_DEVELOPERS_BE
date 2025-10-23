package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.ApiDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiDocumentRepository extends MongoRepository<ApiDocument, String> {
    List<ApiDocument> findByApiIdIn(List<Long> apiIds);

    void deleteByApiId(Long apiId);

    Optional<ApiDocument> findByApiId(Long apiId);
}
