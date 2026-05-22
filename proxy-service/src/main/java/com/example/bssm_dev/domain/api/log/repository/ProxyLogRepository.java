package com.example.bssm_dev.domain.api.log.repository;

import com.example.bssm_dev.domain.api.log.model.ProxyReqResLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProxyLogRepository extends ReactiveMongoRepository<ProxyReqResLog, String> {
}
