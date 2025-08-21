package com.example.bssm_dev.domain.auth.service;

import com.example.bssm_dev.domain.auth.component.UrlBuilder;
import com.example.bssm_dev.domain.auth.dto.response.GoogleLoginUrlResponse;
import com.example.bssm_dev.domain.auth.dto.response.GoogleTokenResponse;
import com.example.bssm_dev.domain.auth.dto.response.GoogleUserResponse;
import com.example.bssm_dev.domain.auth.validator.EmailValidator;
import com.example.bssm_dev.domain.user.dto.request.UserRequest;
import com.example.bssm_dev.domain.user.dto.response.UserLoginResponse;
import com.example.bssm_dev.domain.user.service.UserLoginService;
import com.example.bssm_dev.global.feign.GoogleResourceAccessFeign;
import com.example.bssm_dev.global.feign.GoogleTokenFeign;
import com.example.bssm_dev.global.jwt.JwtProvider;
import com.example.bssm_dev.domain.auth.util.PkceUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {
    private final GoogleResourceAccessFeign googleResourceAccessFeign;
    private final GoogleTokenFeign googleTokenFeign;
    
    private final UrlBuilder urlBuilder;

    private final EmailValidator emailValidator;
    private final UserLoginService userLoginService;
    private final JwtProvider jwtProvider;
    
    public GoogleLoginUrlResponse getUrl(HttpSession session) {
        String codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.codeChallengeS256(codeVerifier);

        session.setAttribute("GOOGLE_CODE_VERIFIER", codeVerifier);
        
        String url = urlBuilder.getGoogleLoginUrl(codeChallenge);

        return new GoogleLoginUrlResponse(url);
    }


    public String registerOrLogin(String code, String codeVerifier) {;
        String body = urlBuilder.getTokenUrl(code, codeVerifier);

        GoogleTokenResponse tokenResponse = googleTokenFeign.getToken(body);

        String authorizationCode = "Bearer " + tokenResponse.access_token();
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
