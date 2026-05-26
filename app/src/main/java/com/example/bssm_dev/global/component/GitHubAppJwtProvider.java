package com.example.bssm_dev.global.component;

import com.example.bssm_dev.global.config.properties.GitHubAppProperties;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * GitHub App 인증에 필요한 App-level JWT를 생성한다.
 * App JWT는 installation access token 교환 시 사용되며, 유효기간은 최대 10분이다.
 *
 * private key는 PKCS#8 PEM 형식이어야 한다.
 * GitHub에서 다운로드한 PKCS#1 키는 아래 명령으로 변환:
 *   openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in rsa.pem -out pkcs8.pem
 */
@Component
@RequiredArgsConstructor
public class GitHubAppJwtProvider {

    private static final long APP_JWT_EXPIRY_SECONDS = 600L;

    private final GitHubAppProperties gitHubAppProperties;

    public String generateAppJwt() {
        PrivateKey privateKey = parsePrivateKey(gitHubAppProperties.getPrivateKey());
        Date now = new Date();
        Date exp = new Date(now.getTime() + APP_JWT_EXPIRY_SECONDS * 1000);

        return Jwts.builder()
                .issuer(gitHubAppProperties.getAppId())
                .issuedAt(now)
                .expiration(exp)
                .signWith(privateKey)
                .compact();
    }

    private PrivateKey parsePrivateKey(String pem) {
        try {
            String cleaned = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(cleaned);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            throw new IllegalStateException("GitHub App private key 파싱 실패. PKCS#8 PEM 형식인지 확인하세요.", e);
        }
    }
}
