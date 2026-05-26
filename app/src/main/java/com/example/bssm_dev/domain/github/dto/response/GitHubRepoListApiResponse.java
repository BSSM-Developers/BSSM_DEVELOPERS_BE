package com.example.bssm_dev.domain.github.dto.response;

import java.util.List;

public record GitHubRepoListApiResponse(
        int total_count,
        List<GitHubRepoItem> repositories
) {}
