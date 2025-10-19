package com.example.bssm_dev.domain.api.requester.impl;

import com.example.bssm_dev.domain.api.model.type.MethodType;
import com.example.bssm_dev.domain.api.requester.Requester;
import org.springframework.web.client.RestClient;

public class RestRequester implements Requester {

    private final RestClient restClient;

    public RestRequester(String domainUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(domainUrl)
                .build();
    }

    public static RestRequester of(String domainUrl) {
        return new RestRequester(domainUrl);
    }

    public Object request(String method, String endpoint) {
        MethodType methodType = MethodType.valueOf(method.toUpperCase());
        Object response = switch (methodType) {
            case GET -> get(endpoint);
            case POST -> post(endpoint);
            case PUT -> put(endpoint);
            case PATCH -> patch(endpoint);
            case DELETE -> delete(endpoint);
        };
        return response;
    }

    @Override
    public Object get(String endpoint) {
        Object response = restClient.get()
                .uri(endpoint)
                .retrieve()
                .body(String.class);
        return response;
    }

    @Override
    public Object post(String endpoint) {
        return null;
    }

    @Override
    public Object put(String endpoint) {
        return null;
    }

    @Override
    public Object patch(String endpoint) {
        return null;
    }

    @Override
    public Object delete(String endpoint) {
        return null;
    }
}
