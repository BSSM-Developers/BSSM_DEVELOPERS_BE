package com.example.bssm_dev.domain.github.dto.response;

public record GitHubConnectionResponse(
        String githubLogin,
        Long installationId,
        boolean appInstalled
) {
    public static GitHubConnectionResponse of(String githubLogin, Long installationId) {
        return new GitHubConnectionResponse(githubLogin, installationId, installationId != null);
    }
}
