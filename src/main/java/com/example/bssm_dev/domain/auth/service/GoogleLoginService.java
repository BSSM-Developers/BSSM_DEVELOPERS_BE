package com.example.bssm_dev.domain.auth.service;

import com.example.bssm_dev.domain.auth.component.UrlBuilder;
import com.example.bssm_dev.domain.auth.dto.response.GoogleLoginUrlResponse;
import com.example.bssm_dev.domain.auth.dto.response.GoogleTokenResponse;
import com.example.bssm_dev.domain.auth.dto.response.GoogleUserResponse;
import com.example.bssm_dev.domain.auth.dto.response.LoginResult;
import com.example.bssm_dev.domain.auth.exception.InvalidStateParameterException;
import com.example.bssm_dev.domain.auth.model.GoogleCodeVerifier;
import com.example.bssm_dev.domain.auth.repository.GoogleCodeVerifierRepository;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    private final GoogleCodeVerifierRepository googleCodeVerifierRepository;

    public GoogleLoginUrlResponse getUrl() {
        String state = UUID.randomUUID().toString();
        String codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.codeChallengeS256(codeVerifier);

        GoogleCodeVerifier googleCodeVerifier = GoogleCodeVerifier.of(state, codeVerifier);
        googleCodeVerifierRepository.save(googleCodeVerifier);

        String url = urlBuilder.getGoogleLoginUrl(codeChallenge, state);

        return new GoogleLoginUrlResponse(url);
    }

    public LoginResult registerOrLogin(String code, String state) {
        GoogleCodeVerifier googleCodeVerifier = googleCodeVerifierRepository.findById(state)
                .orElseThrow(InvalidStateParameterException::raise);

        String codeVerifier = googleCodeVerifier.getCodeVerifier();
        googleCodeVerifierRepository.delete(googleCodeVerifier);

        String body = urlBuilder.getTokenUrl(code, codeVerifier);

        GoogleTokenResponse tokenResponse = googleTokenFeign.getToken(body);

        String authorizationCode = "Bearer " + tokenResponse.access_token();
        GoogleUserResponse googleUser = googleResourceAccessFeign.accessGoogle(authorizationCode);

        String userEmail = googleUser.email();
        boolean isUserExists = userQueryService.isUserExists(userEmail);

        if (isUserExists) {
            // 이미 회원가입 되어있으면 로그인
            return loginUser(userEmail);
        } else {
            // 회원가입 안되어있으면 회원가입부터
            return registerUser(googleUser);
        }
    }

    private LoginResult loginUser(String email) {
        UserLoginResponse userLoginResponse = userQueryService.getUserByEmail(email);

        Long userId = userLoginResponse.userId();
        String userEmail = userLoginResponse.email();
        String role = userLoginResponse.role();

        String refreshToken = jwtProvider.generateRefreshToken(userId, userEmail, role);
        return new LoginResult.LoginSuccess(refreshToken);
    }

    private LoginResult registerUser(GoogleUserResponse googleUser) {
        boolean isBssmEmail = emailValidator.isBssmEmail(googleUser.email());

        if (isBssmEmail) {
            // BSSM 계정이면 바로 회원가입 & 로그인
            UserLoginResponse userLoginResponse = userLoginService.registerIfNotExists(UserRequest.fromGoogleUser(googleUser));

            Long userId = userLoginResponse.userId();
            String email = userLoginResponse.email();
            String role = userLoginResponse.role();

            String refreshToken = jwtProvider.generateRefreshToken(userId, email, role);
            return new LoginResult.LoginSuccess(refreshToken);
        } else {
            // 일반 구글 계정이면 회원가입 신청
            SignupRequest signupRequest = new SignupRequest(
                    googleUser.name(),
                    googleUser.email(),
                    googleUser.picture()
            );

            signupRequestService.createSignupRequest(signupRequest);

            return new LoginResult.SignupRequired();
        }
    }
}
