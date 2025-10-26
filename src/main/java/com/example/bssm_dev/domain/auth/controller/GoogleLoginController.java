package com.example.bssm_dev.domain.auth.controller;
import com.example.bssm_dev.common.dto.ResponseDto;
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

    /**
     * 구글 로그인(Oauth) URL 조회
     */
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
        LoginResult result = googleLoginService.registerOrLogin(code, state);
        String redirectUrl = "";
        switch (result) {
            case LoginResult.LoginSuccess(String refreshToken) -> {
                ResponseCookie cookie = CookieUtil.bake("refresh_token", refreshToken);
                response.addHeader("Set-Cookie", cookie.toString());
                redirectUrl = clientProperties.getLoginSuccessUrl();
            }
            case LoginResult.SignupRequired(String signupToken) -> {
                ResponseCookie cookie = CookieUtil.bake("signup_token", signupToken);
                response.addHeader("Set-Cookie", cookie.toString());
                redirectUrl = clientProperties.getSignupSuccessUrl();
            }
        }
        response.setContentType("text/html;charset=UTF-8");
        String html = """
        <!doctype html>
        <html lang="en">
        <head>
          <meta charset="utf-8" />
          <meta http-equiv="x-ua-compatible" content="ie=edge" />
          <meta name="viewport" content="width=device-width, initial-scale=1" />
          <title>Redirecting…</title>
        </head>
        <body>
          <noscript>
            <meta http-equiv="cookie" content="0;url=%s" />
          </noscript>
          <script>
            window.location.replace("%s");
          </script>
        </body>
        </html>
        """.formatted(redirectUrl, redirectUrl);

        response.getWriter().write(html);
    }
}
