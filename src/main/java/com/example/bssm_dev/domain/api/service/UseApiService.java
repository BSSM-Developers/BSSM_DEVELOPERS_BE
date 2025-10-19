package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.dto.response.ProxyResponse;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.type.MethodType;
import com.example.bssm_dev.domain.api.requester.impl.RestRequester;
import com.example.bssm_dev.domain.api.service.query.ApiTokenQueryService;
import com.example.bssm_dev.domain.api.service.query.ApiUsageQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UseApiService {
    private final ApiTokenQueryService apiTokenQueryService;
    private final ApiUsageQueryService apiUsageQueryService;

    public ProxyResponse get(String secretKey, String token, String endpoint) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateSecretKey(secretKey);

        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.GET);

        Object response = request(endpoint, apiUsage);
        return ProxyResponse.of(response);
    }


    public ProxyResponse post(String secretKey, String token, String endpoint) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateSecretKey(secretKey);

        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.POST);

        Object response = request(endpoint, apiUsage);
        return ProxyResponse.of(response);
    }

    public ProxyResponse patch(String secretKey, String token, String endpoint) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateSecretKey(secretKey);

        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.PATCH);

        Object response = request(endpoint, apiUsage);
        return ProxyResponse.of(response);
    }

    public ProxyResponse put(String secretKey, String token, String endpoint) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateSecretKey(secretKey);

        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.PUT);

        Object response = request(endpoint, apiUsage);
        return ProxyResponse.of(response);
    }

    public ProxyResponse delete(String secretKey, String token, String endpoint) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateSecretKey(secretKey);

        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, MethodType.DELETE);

        Object response = request(endpoint, apiUsage);
        return ProxyResponse.of(response);
    }

    private Object request(String endpoint, ApiUsage apiUsage) {
        String apiDomain = apiUsage.getDomain();
        RestRequester requester = RestRequester.of(apiDomain);
        Object response = requester.request(apiUsage.getMethod(), endpoint);
        log.info(response.toString());
        return response;
    }
}
