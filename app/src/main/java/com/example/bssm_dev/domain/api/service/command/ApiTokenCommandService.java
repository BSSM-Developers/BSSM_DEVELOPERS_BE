package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiTokenAccessException;
import com.example.bssm_dev.domain.api.mapper.ApiTokenMapper;
import com.example.bssm_dev.domain.api.model.ApiToken;

import com.example.bssm_dev.domain.api.repository.ApiTokenRepository;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class ApiTokenCommandService {
    private final ApiTokenRepository apiTokenRepository;
    private final ApiTokenMapper apiTokenMapper;
    private final PasswordEncoder passwordEncoder;

    public SecretApiTokenResponse createApiToken(User user, String apiTokenName, List<String> origins) {
        String plainSecretKey = generateSecretKey();
        String encodedSecretKey = passwordEncoder.encode(plainSecretKey);
        String apiTokenUUID = generateUUID();

        ApiToken apiToken = ApiToken.of(user, encodedSecretKey, apiTokenName, apiTokenUUID);

        if (origins != null && !origins.isEmpty()) {
            origins.forEach(apiToken::addTokenOrigin);
        }

        apiTokenRepository.save(apiToken);

        // 응답에는 평문 secretKey 포함 (사용자가 복사할 수 있도록)
        SecretApiTokenResponse response = apiTokenMapper.toSecretApiTokenResponse(apiToken, plainSecretKey);
        return response;
    }

    public void updateTokenOrigins(User user, Long tokenId, List<String> origins) {
        ApiToken apiToken = apiTokenRepository.findById(tokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        boolean equalsUser = user.equals(apiToken.getUser());
        if (!equalsUser) throw UnauthorizedApiTokenAccessException.raise();

        apiToken.updateTokenOrigins(origins != null ? origins : List.of());
    }

    public SecretApiTokenResponse reGenerateSecretKey(User user, Long tokenId) {
        ApiToken apiToken = apiTokenRepository.findById(tokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        boolean equalsUser = user.equals(apiToken.getUser());
        if (!equalsUser) throw UnauthorizedApiTokenAccessException.raise();

        String plainSecretKey = generateSecretKey();
        String encodedSecretKey = passwordEncoder.encode(plainSecretKey);
        apiToken.changeSecretKey(encodedSecretKey);
        
        // 응답에는 평문 secretKey 포함
        SecretApiTokenResponse response = apiTokenMapper.toSecretApiTokenResponse(apiToken, plainSecretKey);
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
