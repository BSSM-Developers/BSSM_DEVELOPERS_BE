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

    @Override
    public Object get(String endpoint) {
        Object response = restClient.get()
                .uri(endpoint)
                .retrieve()
                .body(Object.class);
        return response;
    }

    @Override
    public Object post(String endpoint, Object body) {
        Object response = restClient.post()
                .uri(endpoint)
                .body(body)
                .retrieve()
                .body(Object.class);
        return response;
    }

    @Override
    public Object put(String endpoint, Object body) {
        Object response = restClient.put()
                .uri(endpoint)
                .body(body)
                .retrieve()
                .body(Object.class);
        return response;
    }

    @Override
    public Object patch(String endpoint, Object body) {
        Object response = restClient.patch()
                .uri(endpoint)
                .body(body)
                .retrieve()
                .body(Object.class);
        return response;
    }

    @Override
    public Object delete(String endpoint, Object body) {
        Object response = restClient.delete()
                .uri(endpoint)
                .retrieve()
                .body(Object.class);
        return response;
    }
}
