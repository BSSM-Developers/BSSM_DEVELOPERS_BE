package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
import com.example.bssm_dev.domain.api.exception.InvalidWebhookSecretException;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.type.ApiTokenState;
import com.example.bssm_dev.domain.api.repository.ApiTokenRepository;
import com.example.bssm_dev.global.config.properties.WebhookProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class ApiTokenWebhookCommandService {
    private final ApiTokenRepository apiTokenRepository;
    private final WebhookProperties webhookProperties;

    public void blockApiToken(String webhookSecret, Long tokenId) {
        validateWebhookSecret(webhookSecret);

        ApiToken apiToken = apiTokenRepository.findById(tokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        if (apiToken.getState() != ApiTokenState.BLOCKED) {
            apiToken.block();
            apiTokenRepository.save(apiToken);
        }
    }

    private void validateWebhookSecret(String webhookSecret) {
        if (webhookSecret == null || !webhookSecret.equals(webhookProperties.getSecret())) {
            throw InvalidWebhookSecretException.raise();
        }
    }
}
