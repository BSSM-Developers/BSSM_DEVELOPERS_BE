package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.dto.request.UseApiRequest;
import com.example.bssm_dev.domain.api.dto.response.ProxyResponse;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.service.query.ApiTokenQueryService;
import com.example.bssm_dev.domain.api.service.query.ApiUsageQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UseApiCommandService {
    private final ApiTokenQueryService apiTokenQueryService;
    private final ApiUsageQueryService apiUsageQueryService;

    public ProxyResponse execute(UseApiRequest useApiRequest, String token, String endpoint) {
        ApiToken apiToken = apiTokenQueryService.findById(token);
        apiToken.validateSecretKey(useApiRequest.secretKey());

        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint);
        String apiDomain = apiUsage.getDomain();

    }
}
