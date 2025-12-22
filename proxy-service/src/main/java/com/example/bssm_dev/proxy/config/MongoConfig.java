package com.example.bssm_dev.proxy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.bssm_dev.domain.api.log.repository")
public class MongoConfig {
}
