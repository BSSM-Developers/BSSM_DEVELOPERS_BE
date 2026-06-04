package com.example.bssm_dev.global.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("jwt")
public class JwtProperties {
    private long accessExp;
    private long refreshExp;
    private String secretKey;
    private String header;
    private String prefix;
}
