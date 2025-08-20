package com.example.bssm_dev.domain.auth.controller;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.domain.auth.dto.response.TokenResponse;
import com.example.bssm_dev.domain.auth.service.GoogleLoginService;
import com.example.bssm_dev.global.config.properties.ClientProperties;
import com.example.bssm_dev.global.util.CookieUtil;
import com.example.bssm_dev.global.util.HttpUtil;
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
    public ResponseEntity<ResponseDto<String>> showGoogleLoginUrl() {
        String url = googleLoginService.getUrl();
        ResponseDto<String> responseDto = HttpUtil.success(url);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/callback")
    public void googleLoginCallback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response) throws IOException {
        TokenResponse tokenResponse = googleLoginService.registerOrLogin(code, state);

        String accessToken = tokenResponse.accessToken();
        response.setHeader("Authorization", "Bearer " + accessToken);

        String refreshToken = tokenResponse.refreshToken();
        Cookie cookie = CookieUtil.bake("refreshToken", refreshToken);
        response.addCookie(cookie);

        response.sendRedirect(clientProperties.getUrl() + state);
    }
}
