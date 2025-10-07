package com.example.bssm_dev.domain.auth.component;
import com.example.bssm_dev.global.config.properties.GoogleOauthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class UrlBuilder {
    private static final String BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String SCOPE = "email profile";
    private final GoogleOauthProperties googleOauthProperties;

    public String getGoogleLoginUrl(String codeChallenge, String state) {
        String redirectUri = googleOauthProperties.getRedirectUri();
        String clientId = googleOauthProperties.getClientId();

        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", SCOPE)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .queryParam("state", state)
                .toUriString();
        return url;
    }

    public String getTokenUrl(String code, String codeVerifier) {
        String body = "grant_type=" + enc("authorization_code")
                + "&code=" + enc(code)
                + "&client_id=" + enc(googleOauthProperties.getClientId())
                + "&redirect_uri=" + enc(googleOauthProperties.getRedirectUri())
                + "&code_verifier=" + enc(codeVerifier)
                + "&client_secret=" + enc(googleOauthProperties.getSecretKey());
        return body;
    }

    private String enc(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }

}
