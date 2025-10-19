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

        MethodType methodType = MethodType.GET;
        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, methodType);

        Object response = request(endpoint, apiUsage, methodType, null);
        return ProxyResponse.of(response);
    }


    public ProxyResponse post(String secretKey, String token, String endpoint, Object body) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateSecretKey(secretKey);

        MethodType methodType = MethodType.POST;
        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, methodType);

        Object response = request(endpoint, apiUsage, methodType, body);
        return ProxyResponse.of(response);
    }

    public ProxyResponse patch(String secretKey, String token, String endpoint, Object body) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateSecretKey(secretKey);

        MethodType methodType = MethodType.PATCH;
        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, methodType);

        Object response = request(endpoint, apiUsage, methodType, body);
        return ProxyResponse.of(response);
    }

    public ProxyResponse put(String secretKey, String token, String endpoint, Object body) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateSecretKey(secretKey);

        MethodType methodType = MethodType.PUT;
        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, methodType);

        Object response = request(endpoint, apiUsage, methodType, body);
        return ProxyResponse.of(response);
    }

    public ProxyResponse delete(String secretKey, String token, String endpoint) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateSecretKey(secretKey);

        MethodType methodType = MethodType.DELETE;
        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, endpoint, methodType);

        Object response = request(endpoint, apiUsage, methodType, null);
        return ProxyResponse.of(response);
    }

    private Object request(String endpoint, ApiUsage apiUsage, MethodType methodType, Object body) {
        String apiDomain = apiUsage.getDomain();
        RestRequester requester = RestRequester.of(apiDomain);
        Object response = switch (methodType) {
            case GET -> requester.get(endpoint);
            case POST -> requester.post(endpoint, body);
            case PUT -> requester.put(endpoint, body);
            case PATCH -> requester.patch(endpoint, body);
            case DELETE -> requester.delete(endpoint);
        };
        log.info(response.toString());
        return response;
    }
}
