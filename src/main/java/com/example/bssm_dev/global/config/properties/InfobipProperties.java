package com.example.bssm_dev.global.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("infobip")
public class InfobipProperties {
    private String url;
    private String apiKey;
}
