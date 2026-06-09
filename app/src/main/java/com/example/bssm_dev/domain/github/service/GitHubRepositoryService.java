package com.example.bssm_dev.domain.github.service;

import com.example.bssm_dev.domain.github.dto.response.GitHubRepoItem;
import com.example.bssm_dev.domain.github.exception.GitHubAppNotInstalledException;
import com.example.bssm_dev.domain.github.exception.GitHubConnectionNotFoundException;
import com.example.bssm_dev.domain.github.exception.GitHubRepositoryNotFoundException;
import com.example.bssm_dev.domain.github.exception.GitHubRepositoryUnauthorizedException;
import com.example.bssm_dev.domain.github.model.GitHubRepository;
import com.example.bssm_dev.domain.github.repository.GitHubInstallationRepository;
import com.example.bssm_dev.domain.github.repository.GitHubRepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubRepositoryService {

    private final GitHubInstallationRepository gitHubInstallationRepository;
    private final GitHubRepositoryRepository gitHubRepositoryRepository;
    private final GitHubApiService gitHubApiService;

    public record FindOrCreateResult(GitHubRepository repo, boolean created) {}

    @Transactional("transactionManager")
    public FindOrCreateResult findOrCreate(Long userId, String repoFullName, String branch) {
        return gitHubRepositoryRepository
                .findByUserIdAndRepoFullNameAndBranch(userId, repoFullName, branch)
                .map(repo -> new FindOrCreateResult(repo, false))
                .orElseGet(() -> {
                    List<Long> installationIds = getInstallationIdsOrThrow(userId);
                    Long installationId = gitHubApiService.findInstallationIdForRepo(installationIds, repoFullName);
                    GitHubRepository saved = gitHubRepositoryRepository.save(
                            GitHubRepository.of(userId, installationId, repoFullName, branch)
                    );
                    return new FindOrCreateResult(saved, true);
                });
    }

    @Transactional("transactionManager")
    public void delete(Long userId, Long repoId) {
        GitHubRepository repo = gitHubRepositoryRepository.findById(repoId)
                .orElseThrow(GitHubRepositoryNotFoundException::raise);

        if (!repo.isOwnedBy(userId)) {
            throw GitHubRepositoryUnauthorizedException.raise();
        }

        gitHubRepositoryRepository.delete(repo);
    }

    private List<Long> getInstallationIdsOrThrow(Long userId) {
        List<Long> ids = gitHubInstallationRepository.findAllByUserId(userId).stream()
                .map(installation -> installation.getInstallationId())
                .toList();
        if (ids.isEmpty()) {
            throw GitHubAppNotInstalledException.raise();
        }
        return ids;
    }
}
