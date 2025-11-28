package com.example.bssm_dev.common.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie bake(
            String k,
            String v
    ) {
        ResponseCookie cookie = ResponseCookie.from(k, v)
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        return cookie;
    }

    private CookieUtil() {}
}
