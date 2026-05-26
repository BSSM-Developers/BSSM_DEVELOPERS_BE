package com.example.bssm_dev.global.component;

import com.example.bssm_dev.domain.github.exception.GitHubInstallationTokenException;
import com.example.bssm_dev.global.feign.GitHubAppApiFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * App JWT를 이용해 GitHub App Installation Access Token을 발급한다.
 * Installation Token은 특정 설치에 대해 GitHub API를 호출할 때 사용되며 유효기간은 1시간이다.
 */
@Component
@RequiredArgsConstructor
public class GitHubInstallationTokenProvider {

    private static final String GITHUB_API_ACCEPT = "application/vnd.github+json";

    private final GitHubAppJwtProvider gitHubAppJwtProvider;
    private final GitHubAppApiFeign gitHubAppApiFeign;

    public String getInstallationToken(Long installationId) {
        try {
            String appJwt = "Bearer " + gitHubAppJwtProvider.generateAppJwt();
            return gitHubAppApiFeign
                    .getInstallationToken(appJwt, GITHUB_API_ACCEPT, installationId)
                    .token();
        } catch (Exception e) {
            throw GitHubInstallationTokenException.raise();
        }
    }
}
