package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.exception.ApiNotFoundException;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.repository.ApiRepository;
import com.example.bssm_dev.domain.api.repository.ApiTokenRepository;
import com.example.bssm_dev.domain.api.repository.ApiUseReasonRepository;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiUsageService {
    private final ApiUsageRepository apiUsageRepository;
    private final ApiRepository apiRepository;
    private final ApiTokenRepository apiTokenRepository;
    private final ApiUseReasonRepository apiUseReasonRepository;
    private final UserRepository userRepository;

    public void createApiUsage(Long apiUseReasonId, Long apiId, Long userId) {
        Api api = apiRepository.findById(apiId)
                .orElseThrow(ApiNotFoundException::raise);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        ApiUseReason apiUseReason = apiUseReasonRepository.findById(apiUseReasonId)
                .orElseThrow(() -> new GlobalException(ErrorCode.API_USE_REASON_NOT_FOUND));

        // ApiToken이 없으면 생성
        ApiToken apiToken = apiTokenRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    ApiToken newToken = ApiToken.of(user, generateSecretKey());
                    return apiTokenRepository.save(newToken);
                });

        // ApiUsage 생성
        ApiUsage apiUsage = ApiUsage.of(
                apiToken,
                api,
                apiUseReason,
                api.getName(),
                api.getEndpoint()
        );

        apiUsageRepository.save(apiUsage);
    }

    private String generateSecretKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
