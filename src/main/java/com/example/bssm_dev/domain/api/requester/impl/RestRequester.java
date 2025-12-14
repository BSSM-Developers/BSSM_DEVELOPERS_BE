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
import org.springframework.web.client.HttpStatusCodeException;

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
        log.info("========== [External API Request] ==========");
        log.info("Method: GET");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: (none)");
        
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
            
            log.info("========== [External API Response] ==========");
            log.info("Method: GET");
            log.info("Endpoint: {}", endpoint);
            log.info("Status: Success");
            log.info("Response Body: {}", response);
            log.info("=============================================");
            
            return parseResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            logError("GET", endpoint, e);
            throw wrapExternal(e);
        }
    }

    @Override
    public Object post(String endpoint, Object body, Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: POST");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: {}", body);
        
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
            
            log.info("========== [External API Response] ==========");
            log.info("Method: POST");
            log.info("Endpoint: {}", endpoint);
            log.info("Status: Success");
            log.info("Response Body: {}", response);
            log.info("=============================================");
            
            return parseResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            logError("POST", endpoint, e);
            throw wrapExternal(e);
        }
    }

    @Override
    public Object put(String endpoint, Object body, Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: PUT");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: {}", body);
        
        try {
            var requestSpec = restClient.put()
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
            
            log.info("========== [External API Response] ==========");
            log.info("Method: PUT");
            log.info("Endpoint: {}", endpoint);
            log.info("Status: Success");
            log.info("Response Body: {}", response);
            log.info("=============================================");
            
            return parseResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            logError("PUT", endpoint, e);
            throw wrapExternal(e);
        }
    }

    @Override
    public Object patch(String endpoint, Object body, Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: PATCH");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: {}", body);
        
        try {
            var requestSpec = restClient.patch()
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
            
            log.info("========== [External API Response] ==========");
            log.info("Method: PATCH");
            log.info("Endpoint: {}", endpoint);
            log.info("Status: Success");
            log.info("Response Body: {}", response);
            log.info("=============================================");
            
            return parseResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            logError("PATCH", endpoint, e);
            throw wrapExternal(e);
        }
    }

    @Override
    public Object delete(String endpoint, Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: DELETE");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: (none)");
        
        try {
            var requestSpec = restClient.delete()
                    .uri(endpoint);
            
            if (headers != null) {
                requestSpec = requestSpec.headers(httpHeaders -> {
                    headers.forEach(httpHeaders::add);
                });
            }
            
            String response = requestSpec
                    .retrieve()
                    .body(String.class);
            
            log.info("========== [External API Response] ==========");
            log.info("Method: DELETE");
            log.info("Endpoint: {}", endpoint);
            log.info("Status: Success");
            log.info("Response Body: {}", response);
            log.info("=============================================");
            
            return parseResponse(response);
        } catch (HttpClientErrorException | HttpServerErrorException | IllegalArgumentException e) {
            logError("DELETE", endpoint, e);
            throw wrapExternal(e);
        }
    }

    private void logError(String method, String endpoint, Exception e) {
        log.error("========== [External API Error] ==========");
        log.error("Method: {}", method);
        log.error("Endpoint: {}", endpoint);
        if (e instanceof HttpStatusCodeException statusEx) {
            log.error("Status: {}", statusEx.getStatusCode());
            log.error("Response Body: {}", statusEx.getResponseBodyAsString());
        } else {
            log.error("Error: {}", e.getMessage(), e);
        }
        log.error("==========================================");
    }

    private ExternalApiException wrapExternal(Exception e) {
        if (e instanceof HttpStatusCodeException statusEx) {
            return ExternalApiException.raise(
                    statusEx.getStatusCode().value(),
                    statusEx.getResponseBodyAsString(),
                    statusEx.getMessage()
            );
        }
        return ExternalApiException.raise(e.getMessage());
    }
}
