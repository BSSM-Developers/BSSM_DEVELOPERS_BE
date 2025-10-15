package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.mapper.ApiUsageMapper;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiUsageCommandService {
    private final ApiUsageRepository apiUsageRepository;
    private final ApiUsageMapper apiUsageMapper;

    public void createApiUsage(Api api, ApiToken apiToken, ApiUseReason apiUseReason) {
        // ApiUsage 생성
        ApiUsage apiUsage = apiUsageMapper.toApiUsage(apiToken, api, apiUseReason);
        apiUsageRepository.save(apiUsage);
    }

}
