package com.example.bssm_dev.global.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("github")
public class GitHubAppProperties {
    private String clientId;
    private String clientSecret;
    private String appSlug;
    private String appId;
    /** PKCS#8 PEM 형식 RSA private key (openssl pkcs8 -topk8 -nocrypt 로 변환) */
    private String privateKey;
    private String webhookSecret;
    private String redirectUri;
}
