package com.example.bssm_dev.domain.api.requester.impl;

import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.domain.api.requester.Requester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
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
        try {
            Object response = restClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .body(String.class);
            log.info("response : {}", response );
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }

    @Override
    public Object post(String endpoint, Object body) {
        try {
            var requestSpec = restClient.post()
                    .uri(endpoint);
            
            if (body != null) requestSpec = requestSpec.body(body);

            
            Object response = requestSpec
                    .retrieve()
                    .body(String.class);
            log.info("response : {}", response );
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }

    @Override
    public Object put(String endpoint, Object body) {
        try {
            var requestSpec = restClient.put()
                    .uri(endpoint);
            
            if (body != null) requestSpec = requestSpec.body(body);

            
            Object response = requestSpec
                    .retrieve()
                    .body(String.class);
            log.info("response : {}", response );
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }

    @Override
    public Object patch(String endpoint, Object body) {
        try {
            var requestSpec = restClient.patch()
                    .uri(endpoint);
            
            if (body != null) requestSpec = requestSpec.body(body);

            
            Object response = requestSpec
                    .retrieve()
                    .body(String.class);
            log.info("response : {}", response );
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }

    @Override
    public Object delete(String endpoint) {
        try {
            Object response = restClient.delete()
                    .uri(endpoint)
                    .retrieve()
                    .body(String.class);
            log.info("response : {}", response );
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }
}
