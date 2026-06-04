package com.example.bssm_dev.domain.github.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "레포지토리 등록 요청")
public record RegisterGitHubRepoRequest(
        @Schema(description = "레포지토리 전체 이름 (owner/repo 형식)", example = "BSSM-Developers/BSSM_DEVELOPERS_BE")
        @NotBlank(message = "레포지토리 이름은 필수입니다. (예: owner/repo)")
        String repoFullName,

        @Schema(description = "추적할 브랜치명", example = "main")
        @NotBlank(message = "브랜치명은 필수입니다.")
        String branch
) {}
