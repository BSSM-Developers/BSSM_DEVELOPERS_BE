package com.example.bssm_dev.domain.github.service;

import com.example.bssm_dev.domain.github.dto.response.GitHubBranchItem;
import com.example.bssm_dev.domain.github.dto.response.GitHubRepoItem;
import com.example.bssm_dev.domain.github.dto.response.GitHubRepoListApiResponse;
import com.example.bssm_dev.global.component.GitHubInstallationTokenProvider;
import com.example.bssm_dev.global.feign.GitHubInstallationApiFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * GitHub App Installation Token을 이용한 GitHub API 호출을 담당한다.
 * 외부 API 호출 로직을 캡슐화해 다른 서비스가 GitHub API 상세에 의존하지 않도록 한다.
 */
@Service
@RequiredArgsConstructor
public class GitHubApiService {

    private static final String GITHUB_API_ACCEPT = "application/vnd.github+json";
    private static final int REPOS_PER_PAGE = 100;
    private static final int BRANCHES_PER_PAGE = 100;

    private final GitHubInstallationTokenProvider installationTokenProvider;
    private final GitHubInstallationApiFeign gitHubInstallationApiFeign;

    public List<GitHubRepoItem> getInstallationRepositories(Long installationId) {
        String token = bearerToken(installationTokenProvider.getInstallationToken(installationId));
        List<GitHubRepoItem> all = new ArrayList<>();
        int page = 1;
        while (true) {
            GitHubRepoListApiResponse response = gitHubInstallationApiFeign
                    .getInstallationRepositories(token, GITHUB_API_ACCEPT, REPOS_PER_PAGE, page);
            List<GitHubRepoItem> batch = response.repositories();
            if (batch == null || batch.isEmpty()) {
                break;
            }
            all.addAll(batch);
            if (all.size() >= response.total_count()) {
                break;
            }
            page++;
        }
        return all;
    }

    public List<GitHubBranchItem> getBranches(Long installationId, String owner, String repo) {
        String token = bearerToken(installationTokenProvider.getInstallationToken(installationId));
        List<GitHubBranchItem> all = new ArrayList<>();
        int page = 1;
        while (true) {
            List<GitHubBranchItem> batch = gitHubInstallationApiFeign
                    .getBranches(token, GITHUB_API_ACCEPT, owner, repo, BRANCHES_PER_PAGE, page);
            if (batch == null || batch.isEmpty()) {
                break;
            }
            all.addAll(batch);
            if (batch.size() < BRANCHES_PER_PAGE) {
                break;
            }
            page++;
        }
        return all;
    }

    public List<GitHubRepoItem> getAllInstallationRepositories(List<Long> installationIds) {
        return installationIds.stream()
                .flatMap(id -> getInstallationRepositories(id).stream())
                .toList();
    }

    public Long findInstallationIdForRepo(List<Long> installationIds, String repoFullName) {
        return installationIds.stream()
                .filter(id -> getInstallationRepositories(id).stream()
                        .anyMatch(repo -> repo.full_name().equals(repoFullName)))
                .findFirst()
                .orElseThrow(com.example.bssm_dev.domain.github.exception.GitHubRepositoryUnauthorizedException::raise);
    }

    private String bearerToken(String token) {
        return "Bearer " + token;
    }
}
