package com.example.bssm_dev.domain.github.service;

import com.example.bssm_dev.domain.api.repository.ApiRepository;
import com.example.bssm_dev.domain.github.dto.request.EndpointParseRequest;
import com.example.bssm_dev.domain.github.dto.response.EndpointParseResponse;
import com.example.bssm_dev.domain.github.dto.response.GitHubBranchItem;
import com.example.bssm_dev.domain.github.dto.response.GitHubRepoItem;
import com.example.bssm_dev.domain.github.dto.response.ParsedEndpointResponse;
import com.example.bssm_dev.domain.github.dto.response.RegisteredRepoResponse;
import com.example.bssm_dev.domain.github.exception.GitHubAppNotInstalledException;
import com.example.bssm_dev.domain.github.exception.GitHubConnectionNotFoundException;
import com.example.bssm_dev.domain.github.exception.GitHubRepositoryNotFoundException;
import com.example.bssm_dev.domain.github.exception.GitHubRepositoryUnauthorizedException;
import com.example.bssm_dev.domain.github.model.GitHubRepository;
import com.example.bssm_dev.domain.github.repository.GitHubInstallationRepository;
import com.example.bssm_dev.domain.github.repository.GitHubRepositoryRepository;
import com.example.bssm_dev.global.component.GitHubInstallationTokenProvider;
import com.example.bssm_dev.global.feign.EndpointParserFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GitHubRepositoryQueryService {

    private final GitHubInstallationRepository gitHubInstallationRepository;
    private final GitHubRepositoryRepository gitHubRepositoryRepository;
    private final GitHubApiService gitHubApiService;
    private final ApiRepository apiRepository;
    private final GitHubInstallationTokenProvider installationTokenProvider;
    private final EndpointParserFeign endpointParserFeign;

    @Transactional(readOnly = true)
    public List<RegisteredRepoResponse> getRegisteredRepositories(Long userId) {
        return gitHubRepositoryRepository.findAllByUserId(userId).stream()
                .map(RegisteredRepoResponse::from)
                .toList();
    }

    public List<GitHubRepoItem> getInstallationRepositories(Long userId) {
        List<Long> installationIds = getInstallationIdsOrThrow(userId);
        return gitHubApiService.getAllInstallationRepositories(installationIds);
    }

    public List<GitHubBranchItem> getBranches(Long userId, String owner, String repo) {
        String repoFullName = owner + "/" + repo;
        Long installationId = resolveInstallationId(userId, repoFullName);
        return gitHubApiService.getBranches(installationId, owner, repo);
    }

    @Transactional(readOnly = true)
    public GitHubRepository getRepositoryForDocs(Long userId, Long repoId) {
        GitHubRepository gitHubRepository = gitHubRepositoryRepository.findById(repoId)
                .orElseThrow(GitHubRepositoryNotFoundException::raise);

        if (!gitHubRepository.isOwnedBy(userId)) {
            throw GitHubRepositoryUnauthorizedException.raise();
        }

        return gitHubRepository;
    }

    public List<ParsedEndpointResponse> getParsedEndpoints(Long userId, String repoFullName, String branch) {
        Optional<GitHubRepository> repoOpt = gitHubRepositoryRepository
                .findByUserIdAndRepoFullNameAndBranch(userId, repoFullName, branch);

        if (repoOpt.isPresent()) {
            List<ParsedEndpointResponse> cached = apiRepository
                    .findAllByGithubRepositoryIdAndIsCurrentTrue(repoOpt.get().getId()).stream()
                    .map(ParsedEndpointResponse::from)
                    .toList();
            if (!cached.isEmpty()) {
                return cached;
            }
        }

        Long installationId = resolveInstallationId(userId, repoFullName);
        String installationToken = installationTokenProvider.getInstallationToken(installationId);
        EndpointParseResponse parsed = endpointParserFeign.parse(
                new EndpointParseRequest(repoFullName, branch, installationToken)
        );
        if (parsed == null || parsed.endpoints() == null) {
            return List.of();
        }
        return parsed.endpoints().stream()
                .map(ParsedEndpointResponse::from)
                .toList();
    }

    private Long resolveInstallationId(Long userId, String repoFullName) {
        return gitHubRepositoryRepository.findFirstByUserIdAndRepoFullName(userId, repoFullName)
                .map(GitHubRepository::getInstallationId)
                .orElseGet(() -> gitHubApiService.findInstallationIdForRepo(
                        getInstallationIdsOrThrow(userId), repoFullName
                ));
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
