package com.example.bssm_dev.domain.github.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubRepoItem(
        Long id,
        String full_name,
        String name,
        @JsonProperty("private") boolean isPrivate
) {}
