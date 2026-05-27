package com.example.bssm_dev.domain.github.dto.response;

public record GitHubConnectResponse(
        String githubLogin,
        String installUrl,
        String accessToken
) {
    public static GitHubConnectResponse of(String githubLogin, String installUrl, String accessToken) {
        return new GitHubConnectResponse(githubLogin, installUrl, accessToken);
    }
}
