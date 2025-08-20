package com.example.bssm_dev.global.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("client")
public class ClientProperties {
    private String url;
}
