package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.dto.request.ApiUsageEndpointUpdateRequest;
import com.example.bssm_dev.domain.api.mapper.ApiUsageMapper;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.key.ApiUsageId;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import com.example.bssm_dev.domain.api.service.query.ApiTokenQueryService;
import com.example.bssm_dev.domain.api.exception.ApiUsageNotFoundException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiUsageAccessException;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiUsageCommandService {
    private final ApiUsageRepository apiUsageRepository;
    private final ApiUsageMapper apiUsageMapper;
    private final ApiTokenQueryService apiTokenQueryService;

    public void createApiUsage(Api api, ApiToken apiToken, ApiUseReason apiUseReason) {
        // ApiUsage 생성
        ApiUsage apiUsage = apiUsageMapper.toApiUsage(apiToken, api, apiUseReason);
        apiUsageRepository.save(apiUsage);
    }

    public void save(ApiToken apiToken, Api api, ApiUseReason apiUseReason) {
        ApiUsage apiUsage = apiUsageMapper.toApiUsage(apiToken, api, apiUseReason);
        apiUsageRepository.save(apiUsage);
    }

    public void changeEndpoint(Long apiId, Long apiTokenId, User currentUser, ApiUsageEndpointUpdateRequest apiUsageEndpointUpdateRequest) {
        ApiUsageId apiUsageId = ApiUsageId.create(apiId, apiTokenId);

        ApiUsage apiUsage = apiUsageRepository.findById(apiUsageId)
                .orElseThrow(ApiUsageNotFoundException::raise);

        ApiToken apiToken = apiTokenQueryService.findById(apiTokenId);

        boolean equalsUser = currentUser.equals(apiToken.getUser());
        if (!equalsUser) {
            throw UnauthorizedApiUsageAccessException.raise();
        }

        String endpoint = apiUsageEndpointUpdateRequest.endpoint();
        apiUsage.updateEndpoint(endpoint);
    }
}
