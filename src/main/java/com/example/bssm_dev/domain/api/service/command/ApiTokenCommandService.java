package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiTokenAccessException;
import com.example.bssm_dev.domain.api.mapper.ApiTokenMapper;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.TokenType;
import com.example.bssm_dev.domain.api.repository.ApiTokenRepository;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiTokenCommandService {
    private final ApiTokenRepository apiTokenRepository;
    private final ApiTokenMapper apiTokenMapper;

    public SecretApiTokenResponse createApiToken(User user, String apiTokenName, TokenType tokenType, List<String> domains) {
        validateTokenCreation(tokenType, domains);
        
        String secretKey = generateSecretKey();
        String apiTokenUUID = generateUUID();
        ApiToken apiToken = ApiToken.of(user, secretKey, apiTokenName, apiTokenUUID, tokenType);
        
        if (tokenType == TokenType.BROWSER && domains != null && !domains.isEmpty()) {
            domains.forEach(apiToken::addTokenDomain);
        }
        
        apiTokenRepository.save(apiToken);
        SecretApiTokenResponse response = apiTokenMapper.toSecretApiTokenResponse(apiToken);
        return response;
    }

    private void validateTokenCreation(TokenType tokenType, List<String> domains) {
        if (tokenType == TokenType.BROWSER && (domains == null || domains.isEmpty())) {
            throw new IllegalArgumentException("BROWSER 타입 토큰은 도메인 목록이 필수입니다.");
        }
    }

    public SecretApiTokenResponse reGenerateSecretKey(User user, Long tokenId) {
        ApiToken apiToken = apiTokenRepository.findById(tokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        boolean equalsUser = user.equals(apiToken.getUser());
        if (!equalsUser) throw UnauthorizedApiTokenAccessException.raise();

        String secretKey = generateSecretKey();
        apiToken.changeSecretKey(secretKey);
        SecretApiTokenResponse response = apiTokenMapper.toSecretApiTokenResponse(apiToken);
        return response;
    }

    public void changeApiTokenName(User user, Long tokenId, String apiTokenName) {
        ApiToken apiToken = apiTokenRepository.findById(tokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        boolean equalsUser = user.equals(apiToken.getUser());
        if (!equalsUser) throw UnauthorizedApiTokenAccessException.raise();

        apiToken.changeApiTokenName(apiTokenName);
    }

    private String generateSecretKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
