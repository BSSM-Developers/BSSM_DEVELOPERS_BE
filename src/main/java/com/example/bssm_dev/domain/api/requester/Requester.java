package com.example.bssm_dev.domain.api.requester;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface Requester {
    Mono<ResponseEntity<byte[]>> get(String endpoint, Map<String, String> headers);
    Mono<ResponseEntity<byte[]>> post(String endpoint, Object body, Map<String, String> headers);
    Mono<ResponseEntity<byte[]>> put(String endpoint, Object body, Map<String, String> headers);
    Mono<ResponseEntity<byte[]>> patch(String endpoint, Object body, Map<String, String> headers);
    Mono<ResponseEntity<byte[]>> delete(String endpoint, Map<String, String> headers);
}
