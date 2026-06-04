package com.example.bssm_dev.domain.auth.util;

import com.example.bssm_dev.domain.auth.exception.Base64ConversionException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PkceUtil {
    private static final SecureRandom RNG = new SecureRandom();

    public static String generateCodeVerifier() {
        byte[] code = new byte[32];
        RNG.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    public static String codeChallengeS256(String codeVerifier) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception e) {
            throw Base64ConversionException.raise();
        }
    }

    private PkceUtil() {}
}
