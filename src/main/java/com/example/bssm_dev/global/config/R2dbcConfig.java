package com.example.bssm_dev.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.example.bssm_dev.domain.api.repository.r2dbc")
public class R2dbcConfig {
}
