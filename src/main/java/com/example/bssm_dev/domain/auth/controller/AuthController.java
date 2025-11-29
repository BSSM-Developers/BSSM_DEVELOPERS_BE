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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    private final CookieUtil cookieUtil;

    /**
     * Access Token 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<AccessTokenResponse>> reissue(
            @CookieValue("refresh_token") String refreshToken,
            HttpServletResponse httpServletResponse
    ) {
        TokenResponse tokenResponse = authService.reissue(refreshToken);

        ResponseCookie refreshTokenCookie = cookieUtil.bake("refresh_token", refreshToken);
        httpServletResponse.addHeader("Set-Cookie", refreshTokenCookie.toString());

        AccessTokenResponse accessTokenResponse = AccessTokenResponse.of(tokenResponse.accessToken());
        ResponseDto<AccessTokenResponse> responseDto = HttpUtil.success("reissue success", accessTokenResponse);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(
            @CookieValue("refresh_token") String refreshToken
    ) {
        authService.logout(refreshToken);
        ResponseDto<Void> responseDto = HttpUtil.success("logout success");
        return ResponseEntity.ok(responseDto);
    }
}
