package com.example.bssm_dev.domain.api.requester.impl;

import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.domain.api.requester.Requester;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
public class RestRequester implements Requester {

    private final RestClient restClient;

    public RestRequester(String domainUrl) {
        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setDefaultRequestConfig(
                                org.apache.hc.client5.http.config.RequestConfig.custom()
                                        .setContentCompressionEnabled(true)
                                        .build()
                        )
                        .setUserAgent("BSSM-DEV-API-Client/1.0")
                        .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        
        this.restClient = RestClient.builder()
                .baseUrl(domainUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public static RestRequester of(String domainUrl) {
        return new RestRequester(domainUrl);
    }

    private Object parseResponse(String responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            return responseBody;
        }
        
        // JSON 파싱 시도
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, Object.class);
        } catch (Exception e) {
            // JSON이 아니면 그냥 문자열로 반환
            log.debug("Response is not JSON, returning as string: {}", e.getMessage());
            return responseBody;
        }
    }

    @Override
    public Object get(String endpoint, Map<String, String> headers) {
        log.info("[External API Request] GET {} | Headers: {}", endpoint, headers);
        try {
            var requestSpec = restClient.get()
                    .uri(endpoint);

            if (headers != null) {
                requestSpec = requestSpec.headers(httpHeaders -> {
                    headers.forEach(httpHeaders::add);
                });
            }

            String response = requestSpec
                    .retrieve()
                    .body(String.class);
            log.info("[External API Response] GET {} | Status: Success | Response: {}", endpoint, response);
            return parseResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }

    @Override
    public Object post(String endpoint, Object body, Map<String, String> headers) {
        log.info("[External API Request] POST {} | Headers: {} | Body: {}", endpoint, headers, body);
        try {
            var requestSpec = restClient.post()
                    .uri(endpoint);
            
            if (body != null) requestSpec = requestSpec.body(body);
            
            if (headers != null) {
                requestSpec = requestSpec.headers(httpHeaders -> {
                    headers.forEach(httpHeaders::add);
                });
            }

            
            String response = requestSpec
                    .retrieve()
                    .body(String.class);
            log.info("[External API Response] POST {} | Status: Success | Response: {}", endpoint, response);
            return parseResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }

    @Override
    public Object put(String endpoint, Object body, Map<String, String> headers) {
        log.info("[External API Request] PUT {} | Headers: {} | Body: {}", endpoint, headers, body);
        try {
            var requestSpec = restClient.put()
                    .uri(endpoint);
            
            if (body != null) requestSpec = requestSpec.body(body);
            
            if (headers != null) {
                requestSpec = requestSpec.headers(httpHeaders -> {
                    headers.forEach(httpHeaders::add);
                });
            }

            Object response = requestSpec
                    .retrieve()
                    .body(String.class);
            log.info("[External API Response] PUT {} | Status: Success | Response: {}", endpoint, response);
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }

    @Override
    public Object patch(String endpoint, Object body, Map<String, String> headers) {
        log.info("[External API Request] PATCH {} | Headers: {} | Body: {}", endpoint, headers, body);
        try {
            var requestSpec = restClient.patch()
                    .uri(endpoint);
            
            if (body != null) requestSpec = requestSpec.body(body);
            
            if (headers != null) {
                requestSpec = requestSpec.headers(httpHeaders -> {
                    headers.forEach(httpHeaders::add);
                });
            }

            Object response = requestSpec
                    .retrieve()
                    .body(String.class);
            log.info("[External API Response] PATCH {} | Status: Success | Response: {}", endpoint, response);
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }

    @Override
    public Object delete(String endpoint, Map<String, String> headers) {
        log.info("[External API Request] DELETE {} | Headers: {}", endpoint, headers);
        try {
            var requestSpec = restClient.delete()
                    .uri(endpoint);
            
            if (headers != null) {
                requestSpec = requestSpec.headers(httpHeaders -> {
                    headers.forEach(httpHeaders::add);
                });
            }
            
            Object response = requestSpec
                    .retrieve()
                    .body(String.class);
            log.info("[External API Response] DELETE {} | Status: Success | Response: {}", endpoint, response);
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw ExternalApiException.raise(e.getMessage());
        }
    }
}
