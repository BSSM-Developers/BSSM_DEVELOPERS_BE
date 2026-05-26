package com.example.bssm_dev.domain.github.service;

import com.example.bssm_dev.domain.github.dto.response.GitHubBranchItem;
import com.example.bssm_dev.domain.github.dto.response.GitHubRepoItem;
import com.example.bssm_dev.domain.github.dto.response.RegisteredRepoResponse;
import com.example.bssm_dev.domain.github.exception.GitHubAppNotInstalledException;
import com.example.bssm_dev.domain.github.exception.GitHubConnectionNotFoundException;
import com.example.bssm_dev.domain.github.exception.GitHubRepositoryNotFoundException;
import com.example.bssm_dev.domain.github.exception.GitHubRepositoryUnauthorizedException;
import com.example.bssm_dev.domain.github.model.GitHubConnection;
import com.example.bssm_dev.domain.github.model.GitHubRepository;
import com.example.bssm_dev.domain.github.repository.GitHubConnectionRepository;
import com.example.bssm_dev.domain.github.repository.GitHubRepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubRepositoryQueryService {

    private final GitHubConnectionRepository gitHubConnectionRepository;
    private final GitHubRepositoryRepository gitHubRepositoryRepository;
    private final GitHubApiService gitHubApiService;

    @Transactional(readOnly = true)
    public List<RegisteredRepoResponse> getRegisteredRepositories(Long userId) {
        return gitHubRepositoryRepository.findAllByUserId(userId).stream()
                .map(RegisteredRepoResponse::from)
                .toList();
    }

    public List<GitHubRepoItem> getInstallationRepositories(Long userId) {
        Long installationId = getInstallationIdOrThrow(userId);
        return gitHubApiService.getInstallationRepositories(installationId);
    }

    public List<GitHubBranchItem> getBranches(Long userId, String owner, String repo) {
        Long installationId = getInstallationIdOrThrow(userId);
        return gitHubApiService.getBranches(installationId, owner, repo);
    }

    @Transactional(readOnly = true)
    public GitHubRepository getRepositoryForDocs(Long userId, Long repoId) {
        GitHubRepository repo = gitHubRepositoryRepository.findById(repoId)
                .orElseThrow(GitHubRepositoryNotFoundException::raise);

        if (!repo.isOwnedBy(userId)) {
            throw GitHubRepositoryUnauthorizedException.raise();
        }

        return repo;
    }

    private Long getInstallationIdOrThrow(Long userId) {
        GitHubConnection connection = gitHubConnectionRepository.findByUserId(userId)
                .orElseThrow(GitHubConnectionNotFoundException::raise);

        if (connection.getInstallationId() == null) {
            throw GitHubAppNotInstalledException.raise();
        }
        return connection.getInstallationId();
    }
}
