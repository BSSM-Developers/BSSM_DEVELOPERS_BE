package com.example.bssm_dev.domain.github.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub 레포지토리 브랜치")
public record GitHubBranchItem(
        @Schema(description = "브랜치명", example = "main")
        String name,

        @Schema(description = "보호된 브랜치 여부", example = "true")
        @JsonProperty("protected") boolean isProtected
) {}
