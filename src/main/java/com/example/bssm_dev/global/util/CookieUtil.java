package com.example.bssm_dev.global.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie bake(String k, String v) {
        Cookie cookie = new Cookie(k, v);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        cookie.setSecure(true); // 배포 시에 주석 해제
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }
}
