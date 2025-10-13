package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.ApiDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDocumentRepository extends MongoRepository<ApiDocument, String> {
}
