package com.example.bssm_dev.domain.auth.service;

import com.example.bssm_dev.domain.auth.dto.response.GoogleUserResponse;
import com.example.bssm_dev.domain.auth.dto.response.TokenResponse;
import com.example.bssm_dev.domain.auth.validator.EmailValidator;
import com.example.bssm_dev.domain.user.dto.request.UserRequest;
import com.example.bssm_dev.domain.user.dto.response.UserLoginResponse;
import com.example.bssm_dev.domain.user.service.UserLoginService;
import com.example.bssm_dev.global.config.properties.GoogleOauthProperties;
import com.example.bssm_dev.global.feign.GoogleResourceAccessFeign;
import com.example.bssm_dev.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {
    private final GoogleOauthProperties googleOauthProperties;
    private final GoogleResourceAccessFeign googleResourceAccessFeign;
    private final EmailValidator emailValidator;
    private final UserLoginService userLoginService;
    private final JwtProvider jwtProvider;


    private static final String baseUrl = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String scope = "email profile";

    public String getUrl() {
        String state = UUID.randomUUID().toString();
        String url = baseUrl + "?" +
                "client_id=" + googleOauthProperties.getClientId() +
                "&redirect_uri=" + googleOauthProperties.getRedirectUri() +
                "&response_type=code" +
                "&scope=" + scope +
                "&access_type=offline" +
                "&prompt=consent" +
                "&state=" + state;
        return url;

    }

    public String registerOrLogin(String code, String state) {
        String authorizationCode = "Bearer " + code;
        GoogleUserResponse googleUser = googleResourceAccessFeign.accessGoogle(authorizationCode);
        emailValidator.isBssmEmail(googleUser.email());

        // user id와 role 받아야함
        UserLoginResponse userLoginResponse = userLoginService.registerIfNotExists(UserRequest.fromGoogleUser(googleUser));

        Long userId = userLoginResponse.userId();
        String email = userLoginResponse.email();
        String role = userLoginResponse.role();
        // jwt 발급 후 리다이렉트

        String refreshToken = jwtProvider.generateRefreshToken(userId, email, role);

        return refreshToken;
    }
}
