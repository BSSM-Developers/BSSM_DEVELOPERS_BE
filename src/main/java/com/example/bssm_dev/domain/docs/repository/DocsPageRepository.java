package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.DocsPage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocsPageRepository extends MongoRepository<DocsPage, String> {
}
