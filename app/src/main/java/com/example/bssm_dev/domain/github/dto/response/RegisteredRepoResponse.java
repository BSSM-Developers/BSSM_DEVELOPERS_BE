package com.example.bssm_dev.domain.github.dto.response;

import com.example.bssm_dev.domain.github.model.GitHubRepository;

public record RegisteredRepoResponse(
        Long id,
        String repoFullName,
        String branch,
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
