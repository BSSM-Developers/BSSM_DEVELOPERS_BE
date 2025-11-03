package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.SideBar;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocsSideBarRepository extends MongoRepository<SideBar, String> {
}
