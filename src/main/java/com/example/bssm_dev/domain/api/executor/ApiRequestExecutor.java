package com.example.bssm_dev.domain.api.executor;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.type.MethodType;
import com.example.bssm_dev.domain.api.requester.impl.RestRequester;

public class ApiRequestExecutor {

    public static Object request(String endpoint, ApiUsage apiUsage, MethodType methodType, Object body) {
        String apiDomain = apiUsage.getDomain();
        return request(endpoint, apiDomain, methodType, body);
    }

    public static Object request(Api api, Object body) {
        String apiDomain = api.getDomain();
        String endpoint = api.getEndpoint();
        MethodType methodType = MethodType.valueOf(api.getMethod());
        return request(endpoint, apiDomain, methodType, body);
    }

    private static Object request(String endpoint, String apiDomain, MethodType methodType, Object body) {
        RestRequester requester = RestRequester.of(apiDomain);
        Object response = switch (methodType) {
            case GET -> requester.get(endpoint);
            case POST -> requester.post(endpoint, body);
            case PUT -> requester.put(endpoint, body);
            case PATCH -> requester.patch(endpoint, body);
            case DELETE -> requester.delete(endpoint);
        };
        return response;
    }
}
