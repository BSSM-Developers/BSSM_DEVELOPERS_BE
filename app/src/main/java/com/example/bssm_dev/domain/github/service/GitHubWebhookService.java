package com.example.bssm_dev.domain.github.service;

import com.example.bssm_dev.domain.github.exception.GitHubConnectionNotFoundException;
import com.example.bssm_dev.domain.github.exception.GitHubWebhookSignatureInvalidException;
import com.example.bssm_dev.domain.github.model.GitHubConnection;
import com.example.bssm_dev.domain.github.repository.GitHubConnectionRepository;
import com.example.bssm_dev.global.config.properties.GitHubAppProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubWebhookService {

    private static final String INSTALLATION_EVENT = "installation";
    private static final String PUSH_EVENT = "push";
    private static final String ACTION_CREATED = "created";
    private static final String ACTION_DELETED = "deleted";
    private static final String REFS_HEADS_PREFIX = "refs/heads/";

    private final GitHubConnectionRepository gitHubConnectionRepository;
    private final GitHubAppProperties gitHubAppProperties;
    private final ObjectMapper objectMapper;
    private final EndpointSyncService endpointSyncService;

    @Transactional("transactionManager")
    public void handle(String event, String signature, String payload) {
        verifySignature(payload, signature);

        switch (event) {
            case INSTALLATION_EVENT -> handleInstallation(payload);
            case PUSH_EVENT -> handlePush(payload);
            default -> log.debug("unhandled github webhook event: {}", event);
        }
    }

    private void handleInstallation(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            String action = root.path("action").asText();
            Long installationId = root.path("installation").path("id").asLong();
            Long githubUserId = root.path("installation").path("account").path("id").asLong();
            String accountLogin = root.path("installation").path("account").path("login").asText();
            String accountType = root.path("installation").path("account").path("type").asText();
            Long senderGithubId = root.path("sender").path("id").asLong();

            log.info("github installation event - action={}, installationId={}, accountId={}, accountLogin={}, accountType={}, senderGithubId={}",
                    action, installationId, githubUserId, accountLogin, accountType, senderGithubId);

            Long lookupId = "Organization".equals(accountType) ? senderGithubId : githubUserId;
            log.info("github installation lookup - lookupId={}", lookupId);

            gitHubConnectionRepository.findByGithubId(lookupId).ifPresentOrElse(connection -> {
                if (ACTION_CREATED.equals(action)) {
                    connection.updateInstallationId(installationId);
                    gitHubConnectionRepository.save(connection);
                    log.info("github app installed - githubLogin={}, installationId={}", connection.getGithubLogin(), installationId);
                } else if (ACTION_DELETED.equals(action)) {
                    connection.removeInstallation();
                    gitHubConnectionRepository.save(connection);
                    log.info("github app uninstalled - githubLogin={}", connection.getGithubLogin());
                }
            }, () -> log.warn("github installation event - no connection found for githubId={}", lookupId));
        } catch (Exception e) {
            log.error("failed to handle github installation event", e);
        }
    }

    private void handlePush(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            String ref = root.path("ref").asText();
            Long installationId = root.path("installation").path("id").asLong();

            if (!ref.startsWith(REFS_HEADS_PREFIX)) {
                return;
            }

            String branch = ref.substring(REFS_HEADS_PREFIX.length());
            String repoFullName = root.path("repository").path("full_name").asText();

            GitHubConnection connection = gitHubConnectionRepository.findByInstallationId(installationId)
                    .orElseThrow(GitHubConnectionNotFoundException::raise);

            log.info("github push event - repo={}, branch={}, githubLogin={}", repoFullName, branch, connection.getGithubLogin());

            endpointSyncService.syncOnPush(repoFullName, branch, installationId);
        } catch (GitHubConnectionNotFoundException e) {
            log.warn("received push event but no matching github connection found for installationId");
        } catch (Exception e) {
            log.error("failed to handle github push event", e);
        }
    }

    private void verifySignature(String payload, String signature) {
        if (signature == null || !signature.startsWith("sha256=")) {
            throw GitHubWebhookSignatureInvalidException.raise();
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(
                    gitHubAppProperties.getWebhookSecret().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            mac.init(keySpec);
            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            String expected = "sha256=" + hex;

            if (!MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8),
                    signature.getBytes(StandardCharsets.UTF_8))) {
                throw GitHubWebhookSignatureInvalidException.raise();
            }
        } catch (GitHubWebhookSignatureInvalidException e) {
            throw e;
        } catch (Exception e) {
            throw GitHubWebhookSignatureInvalidException.raise();
        }
    }
}
