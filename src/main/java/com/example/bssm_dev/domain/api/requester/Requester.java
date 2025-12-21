package com.example.bssm_dev.domain.api.requester;

import reactor.core.publisher.Mono;

public interface Requester {
    Mono<Object> get(String endpoint, java.util.Map<String, String> headers);
    Mono<Object> post(String endpoint, Object body, java.util.Map<String, String> headers);
    Mono<Object> put(String endpoint, Object body, java.util.Map<String, String> headers);
    Mono<Object> patch(String endpoint, Object body, java.util.Map<String, String> headers);
    Mono<Object> delete(String endpoint, java.util.Map<String, String> headers);
}
