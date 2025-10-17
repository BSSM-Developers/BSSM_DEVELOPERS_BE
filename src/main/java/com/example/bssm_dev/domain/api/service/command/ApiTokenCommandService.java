package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiTokenAccessException;
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

    public ApiToken createApiToken(User user) {
        String secretKey = generateSecretKey();
        ApiToken apiToken = ApiToken.of(user, secretKey);
        return apiTokenRepository.save(apiToken);
    }

    public ApiToken reGenerateSecretKey(User user, Long tokenId) {
        ApiToken apiToken = apiTokenRepository.findById(tokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        boolean equalsUser = user.equals(apiToken.getUser());
        if (!equalsUser) throw UnauthorizedApiTokenAccessException.raise();

        String secretKey = generateSecretKey();
        apiToken.changeSecretKey(secretKey);
        return apiToken;
    }

    private String generateSecretKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}
