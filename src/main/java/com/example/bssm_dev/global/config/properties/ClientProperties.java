package com.example.bssm_dev.global.config.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("client")
public class ClientProperties {
    private String loginSuccessUrl;
    private String signupSuccessUrl;
}
