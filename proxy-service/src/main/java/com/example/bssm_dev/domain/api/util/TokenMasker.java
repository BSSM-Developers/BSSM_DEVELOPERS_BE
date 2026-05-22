package com.example.bssm_dev.domain.api.util;

public final class TokenMasker {
    private TokenMasker() {}

    public static String mask(String value) {
        if (value == null || value.isBlank()) return "null";
        if (value.length() <= 6) return "***";
        return value.substring(0, 3) + "***" + value.substring(value.length() - 3);
    }
}
