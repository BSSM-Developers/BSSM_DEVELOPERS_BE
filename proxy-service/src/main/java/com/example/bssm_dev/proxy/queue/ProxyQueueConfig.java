package com.example.bssm_dev.proxy.queue;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProxyQueueProperties.class)
public class ProxyQueueConfig {

    @Bean
    public RequestQueue requestQueue(ProxyQueueProperties properties) {
        return new SemaphoreRequestQueue(
                properties.getMaxInflight(),
                properties.getAcquireTimeout()
        );
    }
}
