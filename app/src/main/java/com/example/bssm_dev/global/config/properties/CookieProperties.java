package com.example.bssm_dev.global.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("cookie")
public class CookieProperties {
    private boolean secure;
    private String sameSite;
}
