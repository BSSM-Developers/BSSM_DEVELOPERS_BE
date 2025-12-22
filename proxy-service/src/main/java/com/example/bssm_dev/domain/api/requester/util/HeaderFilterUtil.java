package com.example.bssm_dev.domain.api.requester.util;

import java.util.Set;

public final class HeaderFilterUtil {
    private static final Set<String> HOP_BY_HOP_HEADERS = Set.of(
            "connection",
            "keep-alive",
            "proxy-authenticate",
            "proxy-authorization",
            "te",
            "trailer",
            "transfer-encoding",
            "upgrade"
    );
    private static final Set<String> EXCLUDED_RESPONSE_HEADERS = Set.of(
            "access-control-allow-origin",
            "access-control-allow-credentials",
            "access-control-allow-headers",
            "access-control-allow-methods",
            "access-control-expose-headers",
            "access-control-max-age",
            "vary"
    );

    private HeaderFilterUtil() {
    }

    public static boolean isHopByHopHeader(String headerName) {
        return headerName != null && HOP_BY_HOP_HEADERS.contains(headerName.toLowerCase());
    }

    public static boolean isExcludedResponseHeader(String headerName) {
        return headerName != null && EXCLUDED_RESPONSE_HEADERS.contains(headerName.toLowerCase());
    }
}
