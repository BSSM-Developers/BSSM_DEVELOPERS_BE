package com.example.bssm_dev.domain.auth.controller;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.domain.auth.dto.request.GoogleLoginRequest;
import com.example.bssm_dev.domain.auth.dto.response.GoogleLoginUrlResponse;
import com.example.bssm_dev.domain.auth.dto.response.LoginResult;
import com.example.bssm_dev.domain.auth.service.GoogleLoginService;
import com.example.bssm_dev.global.config.properties.ClientProperties;
import com.example.bssm_dev.common.util.CookieUtil;
import com.example.bssm_dev.common.util.HttpUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/google")
public class GoogleLoginController {
    private final GoogleLoginService googleLoginService;
    private final ClientProperties clientProperties;
    private final CookieUtil cookieUtil;

    /**
     * 구글 로그인(Oauth) URL 조회
     */
    @GetMapping
    public ResponseEntity<ResponseDto<GoogleLoginUrlResponse>> showGoogleLoginUrl() {
        GoogleLoginUrlResponse url = googleLoginService.getUrl();
        ResponseDto<GoogleLoginUrlResponse> responseDto = HttpUtil.success("Get google login url", url);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Google OAuth2 callback - code를 받아 임시 토큰 생성 후 FE로 redirect
     */
    @GetMapping("/callback")
    public void googleLoginCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws IOException {
        String tempToken = googleLoginService.handleCallback(code, state);
        
        // FE로 redirect with temp token
        String redirectUrl = clientProperties.getBaseUrl() + "/auth/callback?token=" + tempToken;
        response.sendRedirect(redirectUrl);
    }

    /**
     * Google 로그인 - FE에서 임시 토큰으로 호출, 실제 토큰 교환 및 쿠키 발급
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> googleLogin(
            @RequestBody GoogleLoginRequest request,
            HttpServletResponse httpServletResponse
    ) {
        LoginResult result = googleLoginService.processLogin(request.token());
        
        switch (result) {
            case LoginResult.LoginSuccess(String refreshToken) -> {
                ResponseCookie cookie = cookieUtil.bake("refresh_token", refreshToken);
                httpServletResponse.addHeader("Set-Cookie", cookie.toString());
            }
            case LoginResult.SignupRequired(String signupToken) -> {
                ResponseCookie cookie = cookieUtil.bake("signup_token", signupToken);
                httpServletResponse.addHeader("Set-Cookie", cookie.toString());
            }
        }
        
        ResponseDto<Void> responseDto = HttpUtil.success("login success");
        return ResponseEntity.ok(responseDto);
    }
}
