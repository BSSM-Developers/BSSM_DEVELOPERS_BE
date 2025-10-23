package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiTokenAccessException;
import com.example.bssm_dev.domain.api.mapper.ApiTokenMapper;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.repository.ApiTokenRepository;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiTokenCommandService {
    private final ApiTokenRepository apiTokenRepository;
    private final ApiTokenMapper apiTokenMapper;

    public ApiTokenResponse createApiToken(User user, String apiTokenName) {
        String secretKey = generateSecretKey();
        String apiTokenUUID = generateUUID();
        ApiToken apiToken = ApiToken.of(user, secretKey, apiTokenName, apiTokenUUID);
        apiTokenRepository.save(apiToken);
        ApiTokenResponse response = apiTokenMapper.toResponse(apiToken);
        return response;
    }

    public ApiTokenResponse reGenerateSecretKey(User user, Long tokenId) {
        ApiToken apiToken = apiTokenRepository.findById(tokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        boolean equalsUser = user.equals(apiToken.getUser());
        if (!equalsUser) throw UnauthorizedApiTokenAccessException.raise();

        String secretKey = generateSecretKey();
        apiToken.changeSecretKey(secretKey);
        ApiTokenResponse response = apiTokenMapper.toResponse(apiToken);
        return response;
    }

    public ApiTokenResponse changeApiTokenName(User user, Long tokenId, String apiTokenName) {
        ApiToken apiToken = apiTokenRepository.findById(tokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        boolean equalsUser = user.equals(apiToken.getUser());
        if (!equalsUser) throw UnauthorizedApiTokenAccessException.raise();

        apiToken.changeApiTokenName(apiTokenName);
        ApiTokenResponse response = apiTokenMapper.toResponse(apiToken);
        return response;
    }

    private String generateSecretKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
