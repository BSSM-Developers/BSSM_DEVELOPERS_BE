package com.example.bssm_dev.global.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("webhook")
public class WebhookProperties {
    private String secret;
}
