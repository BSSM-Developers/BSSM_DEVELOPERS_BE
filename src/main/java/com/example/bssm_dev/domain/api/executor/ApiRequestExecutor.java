package com.example.bssm_dev.domain.api.executor;

import com.example.bssm_dev.domain.api.dto.request.ApiHealthCheckRequest;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiUsageR2dbc;
import com.example.bssm_dev.domain.api.model.type.MethodType;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.requester.impl.RestRequester;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ApiRequestExecutor {

    public static Mono<Object> request(ApiUsage apiUsage, RequestInfo requestInfo) {

        String endpoint = requestInfo.endpoint();
        MethodType methodType = requestInfo.method();
        Object body = requestInfo.body();
        Map<String, String> headers = requestInfo.headers();

        String apiDomain = apiUsage.getDomain();

        return request(endpoint, apiDomain, methodType, body, headers);
    }

    public static Mono<Object> request(ApiUsageR2dbc apiUsage, RequestInfo requestInfo) {

        String endpoint = requestInfo.endpoint();
        MethodType methodType = MethodType.valueOf(apiUsage.getMethod());
        Object body = requestInfo.body();
        Map<String, String> headers = requestInfo.headers();

        String apiDomain = apiUsage.getDomain();

        return request(endpoint, apiDomain, methodType, body, headers);
    }

    public static Mono<Object> request(String endpoint, String method, String domain, RequestInfo requestInfo) {
        MethodType methodType = MethodType.valueOf(method);

        Object body = requestInfo.body();
        Map<String, String> headers = requestInfo.headers();

        return request(endpoint, domain, methodType, body, headers);
    }

    private static Mono<Object> request(String endpoint, String apiDomain, MethodType methodType, Object body, java.util.Map<String, String> headers) {
        RestRequester requester = RestRequester.of(apiDomain);
        return switch (methodType) {
            case GET -> requester.get(endpoint, headers);
            case POST -> requester.post(endpoint, body, headers);
            case PUT -> requester.put(endpoint, body, headers);
            case PATCH -> requester.patch(endpoint, body, headers);
            case DELETE -> requester.delete(endpoint, headers);
        };
    }
}
