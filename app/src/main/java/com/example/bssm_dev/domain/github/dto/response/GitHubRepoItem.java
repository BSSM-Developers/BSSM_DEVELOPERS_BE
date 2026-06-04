package com.example.bssm_dev.domain.github.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub App이 접근 가능한 레포지토리")
public record GitHubRepoItem(
        @Schema(description = "GitHub 레포지토리 ID", example = "123456789")
        Long id,

        @Schema(description = "레포지토리 전체 이름 (owner/repo)", example = "BSSM-Developers/BSSM_DEVELOPERS_BE")
        String full_name,

        @Schema(description = "레포지토리 이름", example = "BSSM_DEVELOPERS_BE")
        String name,

        @Schema(description = "비공개 레포지토리 여부", example = "false")
        @JsonProperty("private") boolean isPrivate
) {}
