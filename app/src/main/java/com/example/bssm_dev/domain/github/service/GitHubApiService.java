package com.example.bssm_dev.domain.github.service;

import com.example.bssm_dev.domain.github.dto.response.GitHubBranchItem;
import com.example.bssm_dev.domain.github.dto.response.GitHubRepoItem;
import com.example.bssm_dev.global.component.GitHubInstallationTokenProvider;
import com.example.bssm_dev.global.feign.GitHubInstallationApiFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GitHub App Installation Token을 이용한 GitHub API 호출을 담당한다.
 * 외부 API 호출 로직을 캡슐화해 다른 서비스가 GitHub API 상세에 의존하지 않도록 한다.
 */
@Service
@RequiredArgsConstructor
public class GitHubApiService {

    private static final String GITHUB_API_ACCEPT = "application/vnd.github+json";

    private final GitHubInstallationTokenProvider installationTokenProvider;
    private final GitHubInstallationApiFeign gitHubInstallationApiFeign;

    public List<GitHubRepoItem> getInstallationRepositories(Long installationId) {
        String token = bearerToken(installationTokenProvider.getInstallationToken(installationId));
        return gitHubInstallationApiFeign
                .getInstallationRepositories(token, GITHUB_API_ACCEPT)
                .repositories();
    }

    public List<GitHubBranchItem> getBranches(Long installationId, String owner, String repo) {
        String token = bearerToken(installationTokenProvider.getInstallationToken(installationId));
        return gitHubInstallationApiFeign.getBranches(token, GITHUB_API_ACCEPT, owner, repo);
    }

    private String bearerToken(String token) {
        return "Bearer " + token;
    }
}
