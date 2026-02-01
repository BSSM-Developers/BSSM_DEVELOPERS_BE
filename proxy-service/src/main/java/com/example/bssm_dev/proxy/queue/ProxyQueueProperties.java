package com.example.bssm_dev.proxy.queue;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "proxy.queue")
public class ProxyQueueProperties {
    private int maxInflight = 80;
    private Duration acquireTimeout = Duration.ofSeconds(1);

    // HRN Priority settings
    private double basePriority = 1.0;
    private double priorityIncrement = 0.2;
    private double maxPriority = 3.0;
}
