package com.example.bssm_dev.domain.github.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub OAuth 연동 결과")
public record GitHubConnectResponse(
        @Schema(description = "연동된 GitHub 사용자명", example = "octocat")
        String githubLogin,

        @Schema(description = "GitHub App 설치 URL — 이 URL로 이동해 App을 설치해야 레포지토리 등록이 가능합니다",
                example = "https://github.com/apps/bssm-dev/installations/new")
        String installUrl,

        @Schema(description = "재발급된 JWT Access Token")
        String accessToken
) {
    public static GitHubConnectResponse of(String githubLogin, String installUrl, String accessToken) {
        return new GitHubConnectResponse(githubLogin, installUrl, accessToken);
    }
}
