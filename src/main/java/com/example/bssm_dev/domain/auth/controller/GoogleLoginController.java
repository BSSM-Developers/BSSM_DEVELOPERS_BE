package com.example.bssm_dev.domain.auth.controller;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.domain.auth.dto.response.GoogleLoginUrlResponse;
import com.example.bssm_dev.domain.auth.service.GoogleLoginService;
import com.example.bssm_dev.global.config.properties.ClientProperties;
import com.example.bssm_dev.common.util.CookieUtil;
import com.example.bssm_dev.common.util.HttpUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/google")
public class GoogleLoginController {
    private final GoogleLoginService googleLoginService;
    private final ClientProperties clientProperties;

    @GetMapping
    public ResponseEntity<ResponseDto<GoogleLoginUrlResponse>> showGoogleLoginUrl() {
        GoogleLoginUrlResponse url = googleLoginService.getUrl();
        ResponseDto<GoogleLoginUrlResponse> responseDto = HttpUtil.success("Get google login url", url);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/callback")
    public void googleLoginCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws IOException {
        String refreshToken = googleLoginService.registerOrLogin(code, state);

        Cookie cookie = CookieUtil.bake("refresh_token", refreshToken);
        response.addCookie(cookie);

        response.sendRedirect(clientProperties.getUrl());
    }
}
