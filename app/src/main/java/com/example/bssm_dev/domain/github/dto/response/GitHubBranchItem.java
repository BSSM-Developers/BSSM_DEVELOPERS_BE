package com.example.bssm_dev.domain.github.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubBranchItem(
        String name,
        @JsonProperty("protected") boolean isProtected
) {}
