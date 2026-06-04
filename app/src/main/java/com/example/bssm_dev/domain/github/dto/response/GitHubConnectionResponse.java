package com.example.bssm_dev.domain.github.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub 연결 상태")
public record GitHubConnectionResponse(
        @Schema(description = "연동된 GitHub 사용자명", example = "octocat")
        String githubLogin,

        @Schema(description = "GitHub App 설치 여부 — false이면 레포지토리 등록 불가", example = "true")
        boolean appInstalled
) {
    public static GitHubConnectionResponse of(String githubLogin, Long installationId) {
        return new GitHubConnectionResponse(githubLogin, installationId != null);
    }
}
