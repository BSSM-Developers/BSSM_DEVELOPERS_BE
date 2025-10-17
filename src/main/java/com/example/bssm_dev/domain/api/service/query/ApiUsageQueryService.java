package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiUsageQueryService {
    private final ApiUsageRepository apiUsageRepository;

    public ApiUsage findByTokenAndEndpoint(ApiToken apiToken, String endpoint) {
        return apiUsageRepository.findByApiTokenAndEndpoint(apiToken, endpoint);
    }
}
