package com.example.bssm_dev.proxy.cache;

public final class CacheKeys {
    private CacheKeys() {}

    private static final String PREFIX = "proxy:";

    public static final String API_TOKEN_PREFIX = PREFIX + "api_token:";
    public static final String TOKEN_DOMAIN_PREFIX = PREFIX + "token_domain:";
    public static final String API_USAGE_PREFIX = PREFIX + "api_usage:";

    public static String apiToken(String clientId) {
        return API_TOKEN_PREFIX + clientId;
    }

    public static String tokenDomain(Long apiTokenId) {
        return TOKEN_DOMAIN_PREFIX + apiTokenId;
    }

    public static String apiUsage(Long apiTokenId, String endpoint) {
        return API_USAGE_PREFIX + apiTokenId + ":" + endpoint;
    }
}
