package com.example.bssm_dev.domain.auth.controller;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.domain.auth.dto.response.GoogleLoginUrlResponse;
import com.example.bssm_dev.domain.auth.service.GoogleLoginService;
import com.example.bssm_dev.global.config.properties.ClientProperties;
import com.example.bssm_dev.common.util.CookieUtil;
import com.example.bssm_dev.common.util.HttpUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<ResponseDto<GoogleLoginUrlResponse>> showGoogleLoginUrl(HttpSession session) {
        GoogleLoginUrlResponse url = googleLoginService.getUrl(session);
        ResponseDto<GoogleLoginUrlResponse> responseDto = HttpUtil.success("Get google login url", url);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/callback")
    public void googleLoginCallback(@RequestParam("code") String code, HttpServletResponse response, HttpSession session) throws IOException {
        String codeVerifier = (String) session.getAttribute("GOOGLE_CODE_VERIFIER");
        session.removeAttribute("GOOGLE_CODE_VERIFIER");

        String refreshToken = googleLoginService.registerOrLogin(code, codeVerifier);

        Cookie cookie = CookieUtil.bake("refreshToken", refreshToken);
        response.addCookie(cookie);

        response.sendRedirect(clientProperties.getUrl());
    }
}
