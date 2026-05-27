package com.example.bssm_dev.domain.github.dto.response;

import com.example.bssm_dev.domain.github.model.GitHubRepository;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "등록된 레포지토리 정보")
public record RegisteredRepoResponse(
        @Schema(description = "등록된 레포지토리 ID", example = "1")
        Long id,

        @Schema(description = "레포지토리 전체 이름 (owner/repo)", example = "BSSM-Developers/BSSM_DEVELOPERS_BE")
        String repoFullName,

        @Schema(description = "추적 중인 브랜치명", example = "main")
        String branch,

        @Schema(description = "레포지토리 URL", example = "https://github.com/BSSM-Developers/BSSM_DEVELOPERS_BE")
        String repositoryUrl
) {
    public static RegisteredRepoResponse from(GitHubRepository repo) {
        return new RegisteredRepoResponse(
                repo.getId(),
                repo.getRepoFullName(),
                repo.getBranch(),
                repo.toRepositoryUrl()
        );
    }
}
