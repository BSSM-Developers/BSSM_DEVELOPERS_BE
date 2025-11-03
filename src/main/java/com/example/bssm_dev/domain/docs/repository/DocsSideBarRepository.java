package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.SideBar;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DocsSideBarRepository extends MongoRepository<SideBar, String> {
    Optional<SideBar> findByDocsId(String docsId);

    void deleteByDocsId(String docsId);
}
