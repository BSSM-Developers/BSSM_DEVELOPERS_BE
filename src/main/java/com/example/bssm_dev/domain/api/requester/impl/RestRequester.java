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

    public Object request(String method, String endpoint) {
        MethodType methodType = MethodType.valueOf(method.toUpperCase());
        Object response = switch (methodType) {
            case GET -> this.restClient.get();
            case POST -> this.restClient.post();
            case PUT -> this.restClient.put();
            case PATCH -> this.restClient.patch();
            case DELETE -> this.restClient.delete();
        };
        return response;
    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public Object post() {
        return null;
    }

    @Override
    public Object put() {
        return null;
    }

    @Override
    public Object patch() {
        return null;
    }

    @Override
    public Object delete() {
        return null;
    }
}
