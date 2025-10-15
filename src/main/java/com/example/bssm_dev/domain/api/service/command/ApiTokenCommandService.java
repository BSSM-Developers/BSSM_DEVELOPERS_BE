package com.example.bssm_dev.domain.api.service.command;

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

    public void createApiToken(User user) {
        String secretKey = generateSecretKey();
        ApiToken apiToken = apiTokenMapper.toApiToken(user, secretKey);
        apiTokenRepository.save(apiToken);
    }

    private String generateSecretKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
