package com.example.bssm_dev.domain.auth.service;

import com.example.bssm_dev.domain.auth.component.UrlBuilder;
import com.example.bssm_dev.domain.auth.dto.response.GoogleLoginUrlResponse;
import com.example.bssm_dev.domain.auth.dto.response.GoogleTokenResponse;
import com.example.bssm_dev.domain.auth.dto.response.GoogleUserResponse;
import com.example.bssm_dev.domain.auth.validator.EmailValidator;
import com.example.bssm_dev.domain.signup.dto.request.SignupRequest;
import com.example.bssm_dev.domain.user.dto.request.UserRequest;
import com.example.bssm_dev.domain.user.dto.response.UserLoginResponse;
import com.example.bssm_dev.domain.user.service.UserLoginService;
import com.example.bssm_dev.domain.signup.service.SignupRequestService;
import com.example.bssm_dev.domain.user.service.UserQueryService;
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
    private final UserQueryService userQueryService;

    private final SignupRequestService signupRequestService;
    private final JwtProvider jwtProvider;
    
    public GoogleLoginUrlResponse getUrl(HttpSession session) {
        String codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.codeChallengeS256(codeVerifier);

        session.setAttribute("GOOGLE_CODE_VERIFIER", codeVerifier);
        
        String url = urlBuilder.getGoogleLoginUrl(codeChallenge);

        return new GoogleLoginUrlResponse(url);
    }

    public String registerOrLogin(String code, String codeVerifier) {
        String body = urlBuilder.getTokenUrl(code, codeVerifier);

        GoogleTokenResponse tokenResponse = googleTokenFeign.getToken(body);

        String authorizationCode = "Bearer " + tokenResponse.access_token();
        GoogleUserResponse googleUser = googleResourceAccessFeign.accessGoogle(authorizationCode);

        boolean isBssmEmail = emailValidator.isBssmEmail(googleUser.email());

        if (isBssmEmail) {
            // BSSM 계정이면 바로 회원가입 & 로그인
            UserLoginResponse userLoginResponse = userLoginService.registerIfNotExists(UserRequest.fromGoogleUser(googleUser));

            Long userId = userLoginResponse.userId();
            String email = userLoginResponse.email();
            String role = userLoginResponse.role();

            return jwtProvider.generateRefreshToken(userId, email, role);
        } else {
            // 일반 구글 계정인 경우
            String userEmail = googleUser.email();
            return handleGoogleLogin(userEmail, googleUser);
        }
    }

    private String handleGoogleLogin(String userEmail, GoogleUserResponse googleUser) {
        boolean isUserExists = userQueryService.isUserExists(userEmail);

        if (isUserExists) {
            // 회원가입 되어있다면 로그인 성공
            UserLoginResponse userLoginResponse = userQueryService.getUserByEmail(userEmail);

            Long userId = userLoginResponse.userId();
            String email = userLoginResponse.email();
            String role = userLoginResponse.role();

            return jwtProvider.generateRefreshToken(userId, email, role);
        } else {
            // 회원가입이 안되어있다면 회원가입 신청
            SignupRequest signupRequest =
                new SignupRequest(
                    googleUser.picture(),
                    googleUser.email(),
                    googleUser.profile()
                );

            signupRequestService.createSignupRequest(signupRequest);

            // 회원가입 신청 완료 후 적절한 처리 필요 (예: 특별한 토큰이나 리다이렉트)
            return null;
        }
    }
}
