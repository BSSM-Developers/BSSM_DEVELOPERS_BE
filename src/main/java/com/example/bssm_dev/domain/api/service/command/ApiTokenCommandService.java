package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiTokenAccessException;
import com.example.bssm_dev.domain.api.mapper.ApiTokenMapper;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.repository.ApiTokenRepository;
import com.example.bssm_dev.domain.api.service.query.ApiTokenQueryService;
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
    private final ApiTokenQueryService apiTokenQueryService;

    public ApiTokenResponse createApiToken(User user) {
        String secretKey = generateSecretKey();
        ApiToken apiToken = apiTokenMapper.toApiToken(user, secretKey);
        apiTokenRepository.save(apiToken);
        ApiTokenResponse apiTokenResponse = apiTokenMapper.toResponse(apiToken);
        return apiTokenResponse;
    }

    public ApiTokenResponse reGenerateSecretKey(User user, Long tokenId) {
        ApiToken apiToken = apiTokenQueryService.findById(tokenId);

        boolean equalsUser = user.equals(apiToken.getUser());
        if (!equalsUser) {
            throw UnauthorizedApiTokenAccessException.raise();
        }

        String secretKey = generateSecretKey();
        apiToken.changeSecretKey(secretKey);
    }

    private String generateSecretKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}
