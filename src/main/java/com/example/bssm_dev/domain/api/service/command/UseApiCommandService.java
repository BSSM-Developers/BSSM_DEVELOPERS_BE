package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.dto.request.UseApiRequest;
import com.example.bssm_dev.domain.api.dto.response.ProxyResponse;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.requester.Requester;
import com.example.bssm_dev.domain.api.requester.impl.RestRequester;
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

    public ProxyResponse execute(String secretKey, String token, String endpoint) {
        ApiToken apiToken = apiTokenQueryService.findById(token);
        apiToken.validateSecretKey(secretKey);

        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint);
        String apiDomain = apiUsage.getDomain();
        RestRequester requester = RestRequester.of(apiDomain);
        Object response = requester.request(apiUsage.getMethod(), endpoint);
        return ProxyResponse.of(response);
    }
}
