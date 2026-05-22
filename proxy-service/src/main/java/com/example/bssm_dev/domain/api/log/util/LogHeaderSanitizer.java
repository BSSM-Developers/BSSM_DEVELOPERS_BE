package com.example.bssm_dev.domain.api.log.util;

import com.example.bssm_dev.domain.api.util.TokenMasker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class LogHeaderSanitizer {
    private LogHeaderSanitizer() {}

    public static Map<String, String> sanitize(Map<String, String> headers) {
        if (headers == null) return Collections.emptyMap();
        Map<String, String> safe = new HashMap<>();
        headers.forEach((key, value) -> {
            if (key == null) return;
            safe.put(key, isSensitive(key) ? TokenMasker.mask(value) : value);
        });
        return safe;
    }

    private static boolean isSensitive(String headerName) {
        String lower = headerName.toLowerCase();
        return lower.contains("authorization")
                || lower.contains("cookie")
                || lower.contains("token")
                || lower.contains("secret")
                || lower.contains("credential");
    }
}
