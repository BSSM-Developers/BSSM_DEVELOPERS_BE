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
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BrowserUseApiService {
    private final ApiTokenQueryService apiTokenQueryService;
    private final ApiUsageQueryService apiUsageQueryService;
    private final ProxyLogEventPublisher proxyLogEventPublisher;

    public Object get(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }


    public Object post(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }

    public Object patch(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }

    public Object put(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }

    public Object delete(String token, HttpServletRequest request) {
        return handleRequest(token, request);
    }

    private Object handleRequest(String token, HttpServletRequest request) {
        ApiToken apiToken = apiTokenQueryService.findByTokenClientId(token);
        String origin = request.getHeader("Origin");
        apiToken.validateBrowserAccess(origin);

        RequestInfo requestInfo = RequestInfo.of(request);
        ApiUsage apiUsage = apiUsageQueryService.findByTokenAndEndpoint(apiToken, requestInfo);

        long startedAt = System.currentTimeMillis();
        try {
            Object response = ApiRequestExecutor.request(apiUsage, requestInfo);
            proxyLogEventPublisher.publishSuccess(
                    ProxyLogDirection.BROWSER_TO_SERVER,
                    apiToken,
                    requestInfo,
                    request,
                    response,
                    startedAt
            );
            return response;
        } catch (Exception e) {
            proxyLogEventPublisher.publishError(
                    ProxyLogDirection.BROWSER_TO_SERVER,
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
