package com.example.bssm_dev.domain.github.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterGitHubRepoRequest(
        @NotBlank(message = "레포지토리 이름은 필수입니다. (예: owner/repo)")
        String repoFullName,
        @NotBlank(message = "브랜치명은 필수입니다.")
        String branch
) {}
