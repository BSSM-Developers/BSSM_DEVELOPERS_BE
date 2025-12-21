package com.example.bssm_dev.domain.api.requester.impl;

import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.domain.api.requester.Requester;
import com.example.bssm_dev.domain.api.requester.util.RestRequesterLogUtil;
import com.example.bssm_dev.domain.api.requester.util.ResponseEntitySanitizer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RestRequester implements Requester {

    private final WebClient webClient;

    public RestRequester(String domainUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(30))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS))
                );

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16MB
                    configurer.defaultCodecs().enableLoggingRequestDetails(true);
                })
                .build();

        this.webClient = WebClient.builder()
                .baseUrl(domainUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .build();
    }

    public static RestRequester of(String domainUrl) {
        return new RestRequester(domainUrl);
    }

    @Override
    public Mono<ResponseEntity<byte[]>> get(String endpoint, Map<String, String> headers) {
        RestRequesterLogUtil.logRequest("GET", endpoint, null, headers);

        return webClient.get()
                .uri(endpoint)
                .headers(httpHeaders -> applyHeaders(httpHeaders, headers))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(body -> {
                            RestRequesterLogUtil.logErrorResponse("GET", endpoint, response.statusCode().value(), body);
                            return Mono.error(ExternalApiException.raise(
                                    response.statusCode().value(),
                                    body,
                                    "External API error"
                            ));
                        }))
                .toEntity(byte[].class)
                .map(ResponseEntitySanitizer::sanitize)
                .doOnNext(response -> RestRequesterLogUtil.logSuccess("GET", endpoint, response))
                .onErrorMap(IllegalArgumentException.class, e -> {
                    RestRequesterLogUtil.logError("GET", endpoint, e);
                    return RestRequesterLogUtil.wrapExternal(e);
                });
    }

    @Override
    public Mono<ResponseEntity<byte[]>> post(String endpoint, Object body, Map<String, String> headers) {
        RestRequesterLogUtil.logRequest("POST", endpoint, body, headers);

        var builder = webClient.post().uri(endpoint);
        var withBody = (body != null) ? builder.bodyValue(body) : builder;

        return withBody
                .headers(httpHeaders -> applyHeaders(httpHeaders, headers))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(bodyByte -> {
                            RestRequesterLogUtil.logErrorResponse("POST", endpoint, response.statusCode().value(), bodyByte);
                            return Mono.error(ExternalApiException.raise(
                                    response.statusCode().value(),
                                    bodyByte,
                                    "External API error"
                            ));
                        }))
                .toEntity(byte[].class)
                .map(ResponseEntitySanitizer::sanitize)
                .doOnNext(response -> RestRequesterLogUtil.logSuccess("POST", endpoint, response))
                .onErrorMap(IllegalArgumentException.class, e -> {
                    RestRequesterLogUtil.logError("POST", endpoint, e);
                    return RestRequesterLogUtil.wrapExternal(e);
                });
    }

    @Override
    public Mono<ResponseEntity<byte[]>> put(String endpoint, Object body, Map<String, String> headers) {
        RestRequesterLogUtil.logRequest("PUT", endpoint, body, headers);

        var builder = webClient.put().uri(endpoint);

        var withBody = (body != null) ? builder.bodyValue(body) : builder;

        return withBody
                .headers(httpHeaders -> applyHeaders(httpHeaders, headers))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(bodyByte -> {
                            RestRequesterLogUtil.logErrorResponse("PUT", endpoint, response.statusCode().value(), bodyByte);
                            return Mono.error(ExternalApiException.raise(
                                    response.statusCode().value(),
                                    bodyByte,
                                    "External API error"
                            ));
                        }))
                .toEntity(byte[].class)
                .map(ResponseEntitySanitizer::sanitize)
                .doOnNext(response -> RestRequesterLogUtil.logSuccess("PUT", endpoint, response))
                .onErrorMap(IllegalArgumentException.class, e -> {
                    RestRequesterLogUtil.logError("PUT", endpoint, e);
                    return RestRequesterLogUtil.wrapExternal(e);
                });
    }

    @Override
    public Mono<ResponseEntity<byte[]>> patch(String endpoint, Object body, Map<String, String> headers) {
        RestRequesterLogUtil.logRequest("PATCH", endpoint, body, headers);

        var builder = webClient.patch().uri(endpoint);

        var withBody = (body != null) ? builder.bodyValue(body) : builder;

        return withBody
                .headers(httpHeaders -> applyHeaders(httpHeaders, headers))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(bodyByte -> {
                            RestRequesterLogUtil.logErrorResponse("PATCH", endpoint, response.statusCode().value(), bodyByte);
                            return Mono.error(ExternalApiException.raise(
                                    response.statusCode().value(),
                                    bodyByte,
                                    "External API error"
                            ));
                        }))
                .toEntity(byte[].class)
                .map(ResponseEntitySanitizer::sanitize)
                .doOnNext(response -> RestRequesterLogUtil.logSuccess("PATCH", endpoint, response))
                .onErrorMap(IllegalArgumentException.class, e -> {
                    RestRequesterLogUtil.logError("PATCH", endpoint, e);
                    return RestRequesterLogUtil.wrapExternal(e);
                });
    }

    @Override
    public Mono<ResponseEntity<byte[]>> delete(String endpoint, Map<String, String> headers) {
        RestRequesterLogUtil.logRequest("DELETE", endpoint, null, headers);

        return webClient.delete()
                .uri(endpoint)
                .headers(httpHeaders -> applyHeaders(httpHeaders, headers))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(body -> {
                            RestRequesterLogUtil.logErrorResponse("DELETE", endpoint, response.statusCode().value(), body);
                            return Mono.error(ExternalApiException.raise(
                                    response.statusCode().value(),
                                    body,
                                    "External API error"
                            ));
                        }))
                .toEntity(byte[].class)
                .map(ResponseEntitySanitizer::sanitize)
                .doOnNext(response -> RestRequesterLogUtil.logSuccess("DELETE", endpoint, response))
                .onErrorMap(IllegalArgumentException.class, e -> {
                    RestRequesterLogUtil.logError("DELETE", endpoint, e);
                    return RestRequesterLogUtil.wrapExternal(e);
                });
    }

    private void applyHeaders(HttpHeaders httpHeaders, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach(httpHeaders::add);
        }
    }
}
