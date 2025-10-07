package com.example.bssm_dev.domain.auth.controller;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.CookieUtil;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.auth.dto.response.AccessTokenResponse;
import com.example.bssm_dev.domain.auth.dto.response.TokenResponse;
import com.example.bssm_dev.domain.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<AccessTokenResponse>> reissue(
            @CookieValue("refresh_token") String refreshToken,
            HttpServletResponse httpServletResponse
    ) {
        TokenResponse tokenResponse = authService.reissue(refreshToken);

        Cookie refreshTokenCookie = CookieUtil.bake("refresh_token", refreshToken);
        httpServletResponse.addCookie(refreshTokenCookie);

        AccessTokenResponse accessTokenResponse = AccessTokenResponse.of(tokenResponse.accessToken());
        ResponseDto<AccessTokenResponse> responseDto = HttpUtil.success("토큰 재발급 성공", accessTokenResponse);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(
            @CookieValue("refresh_token") String refreshToken
    ) {
        authService.logout(refreshToken);
        ResponseDto<Void> responseDto = HttpUtil.success("로그아웃 성공");
        return ResponseEntity.ok(responseDto);
    }
}
