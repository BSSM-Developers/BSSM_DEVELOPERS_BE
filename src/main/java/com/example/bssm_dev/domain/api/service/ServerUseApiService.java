package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.executor.ApiRequestExecutor;
import com.example.bssm_dev.domain.api.log.model.ProxyLogDirection;
import com.example.bssm_dev.domain.api.log.service.ProxyLogEventPublisher;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.service.query.ApiTokenQueryService;
import com.example.bssm_dev.domain.api.service.query.ApiUsageQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ServerUseApiService {
    private final ApiTokenQueryService apiTokenQueryService;
    private final ApiUsageQueryService apiUsageQueryService;
    private final PasswordEncoder passwordEncoder;
    private final ProxyLogEventPublisher proxyLogEventPublisher;

    public Object get(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Object post(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Object patch(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Object put(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    public Object delete(String secretKey, String token, HttpServletRequest request) {
        return handleRequest(secretKey, token, request);
    }

    private Object handleRequest(String secretKey, String token, HttpServletRequest request) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        apiToken.validateServerAccess(secretKey, passwordEncoder);

        RequestInfo requestInfo = RequestInfo.of(request);

        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, requestInfo);

        long startedAt = System.currentTimeMillis();
        try {
            Object response = ApiRequestExecutor.request(apiUsage, requestInfo);
            proxyLogEventPublisher.publishSuccess(
                    ProxyLogDirection.SERVER_TO_SERVER,
                    apiToken,
                    requestInfo,
                    request,
                    response,
                    startedAt
            );
            return response;
        } catch (Exception e) {
            proxyLogEventPublisher.publishError(
                    ProxyLogDirection.SERVER_TO_SERVER,
                    apiToken,
                    requestInfo,
                    request,
                    e,
                    startedAt
            );
            throw e;
        }
    }
}
