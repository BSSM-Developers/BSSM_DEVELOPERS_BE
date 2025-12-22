package com.example.bssm_dev.global.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("google")
public class GoogleOauthProperties {
    private String clientId;
    private String secretKey;
    private String redirectUri;
}
