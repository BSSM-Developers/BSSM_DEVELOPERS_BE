package com.example.bssm_dev.domain.api.requester.impl;

import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.domain.api.requester.Requester;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RestRequester implements Requester {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RestRequester(String domainUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(30))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS))
                );

        this.webClient = WebClient.builder()
                .baseUrl(domainUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
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
    public Mono<Object> get(String endpoint, Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: GET");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: (none)");

        return webClient.get()
                .uri(endpoint)
                .headers(httpHeaders -> {
                    if (headers != null) {
                        headers.forEach(httpHeaders::add);
                    }
                })
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    log.info("========== [External API Response] ==========");
                    log.info("Method: GET");
                    log.info("Endpoint: {}", endpoint);
                    log.info("Status: Success");
                    log.info("Response Body: {}", response);
                    log.info("=============================================");
                })
                .map(this::parseResponse)
                .onErrorMap(WebClientResponseException.class, e -> {
                    logError("GET", endpoint, e);
                    return wrapExternal(e);
                })
                .onErrorMap(IllegalArgumentException.class, e -> {
                    logError("GET", endpoint, e);
                    return wrapExternal(e);
                });
    }

    @Override
    public Mono<Object> post(String endpoint, Object body, Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: POST");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: {}", body);

        var builder = webClient.post().uri(endpoint);

        var withBody = (body != null) ? builder.bodyValue(body) : builder;

        return withBody
                .headers(httpHeaders -> {
                    if (headers != null) {
                        headers.forEach(httpHeaders::add);
                    }
                })
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    log.info("========== [External API Response] ==========");
                    log.info("Method: POST");
                    log.info("Endpoint: {}", endpoint);
                    log.info("Status: Success");
                    log.info("Response Body: {}", response);
                    log.info("=============================================");
                })
                .map(this::parseResponse)
                .onErrorMap(WebClientResponseException.class, e -> {
                    logError("POST", endpoint, e);
                    return wrapExternal(e);
                })
                .onErrorMap(IllegalArgumentException.class, e -> {
                    logError("POST", endpoint, e);
                    return wrapExternal(e);
                });
    }

    @Override
    public Mono<Object> put(String endpoint, Object body, Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: PUT");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: {}", body);

        var builder = webClient.put().uri(endpoint);

        var withBody = (body != null) ? builder.bodyValue(body) : builder;

        return withBody
                .headers(httpHeaders -> {
                    if (headers != null) {
                        headers.forEach(httpHeaders::add);
                    }
                })
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    log.info("========== [External API Response] ==========");
                    log.info("Method: PUT");
                    log.info("Endpoint: {}", endpoint);
                    log.info("Status: Success");
                    log.info("Response Body: {}", response);
                    log.info("=============================================");
                })
                .map(this::parseResponse)
                .onErrorMap(WebClientResponseException.class, e -> {
                    logError("PUT", endpoint, e);
                    return wrapExternal(e);
                })
                .onErrorMap(IllegalArgumentException.class, e -> {
                    logError("PUT", endpoint, e);
                    return wrapExternal(e);
                });
    }

    @Override
    public Mono<Object> patch(String endpoint, Object body, Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: PATCH");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: {}", body);

        var builder = webClient.patch().uri(endpoint);

        var withBody = (body != null) ? builder.bodyValue(body) : builder;

        return withBody
                .headers(httpHeaders -> {
                    if (headers != null) {
                        headers.forEach(httpHeaders::add);
                    }
                })
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    log.info("========== [External API Response] ==========");
                    log.info("Method: PATCH");
                    log.info("Endpoint: {}", endpoint);
                    log.info("Status: Success");
                    log.info("Response Body: {}", response);
                    log.info("=============================================");
                })
                .map(this::parseResponse)
                .onErrorMap(WebClientResponseException.class, e -> {
                    logError("PATCH", endpoint, e);
                    return wrapExternal(e);
                })
                .onErrorMap(IllegalArgumentException.class, e -> {
                    logError("PATCH", endpoint, e);
                    return wrapExternal(e);
                });
    }

    @Override
    public Mono<Object> delete(String endpoint, Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: DELETE");
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        log.info("Request Body: (none)");

        return webClient.delete()
                .uri(endpoint)
                .headers(httpHeaders -> {
                    if (headers != null) {
                        headers.forEach(httpHeaders::add);
                    }
                })
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    log.info("========== [External API Response] ==========");
                    log.info("Method: DELETE");
                    log.info("Endpoint: {}", endpoint);
                    log.info("Status: Success");
                    log.info("Response Body: {}", response);
                    log.info("=============================================");
                })
                .map(this::parseResponse)
                .onErrorMap(WebClientResponseException.class, e -> {
                    logError("DELETE", endpoint, e);
                    return wrapExternal(e);
                })
                .onErrorMap(IllegalArgumentException.class, e -> {
                    logError("DELETE", endpoint, e);
                    return wrapExternal(e);
                });
    }

    private void logError(String method, String endpoint, Exception e) {
        log.error("========== [External API Error] ==========");
        log.error("Method: {}", method);
        log.error("Endpoint: {}", endpoint);
        if (e instanceof WebClientResponseException webClientEx) {
            log.error("Status: {}", webClientEx.getStatusCode());
            log.error("Response Body: {}", webClientEx.getResponseBodyAsString());
        } else {
            log.error("Error: {}", e.getMessage(), e);
        }
        log.error("==========================================");
    }

    private ExternalApiException wrapExternal(Exception e) {
        if (e instanceof WebClientResponseException webClientEx) {
            return ExternalApiException.raise(
                    webClientEx.getStatusCode().value(),
                    webClientEx.getResponseBodyAsString(),
                    webClientEx.getMessage()
            );
        }
        return ExternalApiException.raise(e.getMessage());
    }
}
