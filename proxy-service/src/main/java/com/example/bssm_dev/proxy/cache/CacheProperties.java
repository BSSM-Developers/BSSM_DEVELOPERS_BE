package com.example.bssm_dev.proxy.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "proxy.cache")
public class CacheProperties {
    private Duration ttl = Duration.ofMinutes(5);
    private int localMaxSize = 1000;
    private Duration localTtl = Duration.ofSeconds(30);
}
