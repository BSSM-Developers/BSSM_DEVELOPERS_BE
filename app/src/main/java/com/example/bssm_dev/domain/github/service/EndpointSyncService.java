package com.example.bssm_dev.domain.github.service;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiGroup;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.TryItToken;
import com.example.bssm_dev.domain.api.model.key.ApiUsageId;
import com.example.bssm_dev.domain.api.repository.ApiGroupRepository;
import com.example.bssm_dev.domain.api.repository.ApiRepository;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import com.example.bssm_dev.domain.api.repository.TryItTokenRepository;
import com.example.bssm_dev.domain.github.dto.request.EndpointParseRequest;
import com.example.bssm_dev.domain.github.dto.response.EndpointParseResponse;
import com.example.bssm_dev.domain.github.model.GitHubConnection;
import com.example.bssm_dev.domain.github.model.GitHubRepository;
import com.example.bssm_dev.domain.github.repository.GitHubConnectionRepository;
import com.example.bssm_dev.domain.github.repository.GitHubRepositoryRepository;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import com.example.bssm_dev.global.component.GitHubInstallationTokenProvider;
import com.example.bssm_dev.global.feign.EndpointParserFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EndpointSyncService {

    private final EndpointParserFeign endpointParserFeign;
    private final GitHubInstallationTokenProvider installationTokenProvider;
    private final GitHubRepositoryRepository gitHubRepositoryRepository;
    private final GitHubConnectionRepository gitHubConnectionRepository;
    private final ApiRepository apiRepository;
    private final ApiGroupRepository apiGroupRepository;
    private final UserRepository userRepository;
    private final TryItTokenRepository tryItTokenRepository;
    private final ApiUsageRepository apiUsageRepository;

    @Transactional("transactionManager")
    public void syncOnPush(String repoFullName, String branch, Long installationId) {
        List<GitHubRepository> registeredRepos = gitHubRepositoryRepository
                .findAllByRepoFullNameAndBranch(repoFullName, branch);

        if (registeredRepos.isEmpty()) {
            log.debug("no registered repos for {}/{}", repoFullName, branch);
            return;
        }

        String installationToken;
        try {
            installationToken = installationTokenProvider.getInstallationToken(installationId);
        } catch (Exception e) {
            log.error("failed to get installation token for installationId={}", installationId, e);
            return;
        }

        EndpointParseResponse parsed;
        try {
            parsed = endpointParserFeign.parse(
                    new EndpointParseRequest(repoFullName, branch, installationToken)
            );
        } catch (Exception e) {
            log.error("endpoint-parser call failed for {}/{}", repoFullName, branch, e);
            return;
        }

        if (parsed == null || parsed.endpoints() == null || parsed.endpoints().isEmpty()) {
            log.info("no endpoints parsed for {}/{}", repoFullName, branch);
            return;
        }

        for (GitHubRepository repo : registeredRepos) {
            syncApiVersions(repo, parsed.endpoints());
        }
    }

    private void syncApiVersions(GitHubRepository repo, List<EndpointParseResponse.ParsedEndpoint> parsedEndpoints) {
        GitHubConnection connection = gitHubConnectionRepository.findByInstallationId(repo.getInstallationId())
                .orElse(null);
        if (connection == null) {
            return;
        }

        Optional<User> creatorOpt = userRepository.findById(repo.getUserId());
        if (creatorOpt.isEmpty()) {
            return;
        }
        User creator = creatorOpt.get();

        for (EndpointParseResponse.ParsedEndpoint parsed : parsedEndpoints) {
            Optional<Api> existingOpt = apiRepository
                    .findCurrentApiByRepoAndEndpointAndMethod(repo.getId(), parsed.path(), parsed.method());

            if (existingOpt.isPresent()) {
                Api existing = existingOpt.get();
                // 내용 변경이 없으면 skip
                if (existing.getEndpoint().equals(parsed.path()) && existing.getMethod().equals(parsed.method())) {
                    continue;
                }
                // 변경된 경우 새 버전 생성
                Api next = existing.nextVersion(UUID.randomUUID().toString());
                apiRepository.save(existing);
                apiRepository.save(next);
                log.info("api version bumped: group={} v{} -> v{}", existing.getApiGroup().getApiGroupId(),
                        existing.getVersion(), next.getVersion());

                // TryItToken ApiUsage endpoint/method 동기화
                syncTryItTokenUsage(existing.getApiGroup().getApiGroupId(), next);
            } else {
                // 신규 엔드포인트
                ApiGroup group = apiGroupRepository.save(ApiGroup.of(UUID.randomUUID().toString()));
                Api newApi = Api.builder()
                        .apiId(UUID.randomUUID().toString())
                        .apiGroup(group)
                        .creator(creator)
                        .endpoint(parsed.path())
                        .method(parsed.method())
                        .name(parsed.method() + " " + parsed.path())
                        .version(1)
                        .isCurrent(true)
                        .validFrom(LocalDateTime.now())
                        .githubRepositoryId(repo.getId())
                        .autoApproval(false)
                        .build();
                apiRepository.save(newApi);
                log.info("new api registered: {} {}", parsed.method(), parsed.path());
            }
        }
    }

    private void syncTryItTokenUsage(String apiGroupId, Api next) {
        tryItTokenRepository.findByApiApiGroupApiGroupId(apiGroupId).ifPresent(tryItToken -> {
            ApiUsageId usageId = ApiUsageId.create(apiGroupId, tryItToken.getApiTokenId());
            apiUsageRepository.findById(usageId).ifPresent(usage -> {
                usage.updateEndpoint(next.getEndpoint());
                usage.updateMethod(next.getMethod());
                log.info("TryItToken usage synced: group={} endpoint={} method={}",
                        apiGroupId, next.getEndpoint(), next.getMethod());
            });
        });
    }
}
