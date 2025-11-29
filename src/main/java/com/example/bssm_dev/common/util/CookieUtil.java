package com.example.bssm_dev.common.util;

import com.example.bssm_dev.global.config.properties.CookieProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final CookieProperties cookieProperties;

    public ResponseCookie bake(String k, String v) {
        ResponseCookie cookie = ResponseCookie.from(k, v)
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite(cookieProperties.getSameSite())
                .build();
        return cookie;
    }
}
