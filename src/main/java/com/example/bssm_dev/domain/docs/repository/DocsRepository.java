package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.Docs;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocsRepository extends MongoRepository<Docs, String> {
}
