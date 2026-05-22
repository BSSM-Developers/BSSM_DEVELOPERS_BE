package com.example.bssm_dev.domain.api.controller;

import com.example.bssm_dev.domain.api.service.BrowserUseApiService;
import com.example.bssm_dev.domain.api.service.ServerUseApiService;
import com.example.bssm_dev.domain.api.util.RequestBodyReader;
import com.example.bssm_dev.domain.api.util.TokenMasker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UseApiController {
    private final BrowserUseApiService browserUseApiService;
    private final ServerUseApiService serverUseApiService;

    @GetMapping("/**")
    public Mono<ResponseEntity<byte[]>> get(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        return dispatch(request, token, secretKey);
    }

    @PostMapping("/**")
    public Mono<ResponseEntity<byte[]>> post(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        return dispatch(request, token, secretKey);
    }

    @PatchMapping("/**")
    public Mono<ResponseEntity<byte[]>> patch(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        return dispatch(request, token, secretKey);
    }

    @PutMapping("/**")
    public Mono<ResponseEntity<byte[]>> put(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        return dispatch(request, token, secretKey);
    }

    @DeleteMapping("/**")
    public Mono<ResponseEntity<byte[]>> delete(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        return dispatch(request, token, secretKey);
    }

    private Mono<ResponseEntity<byte[]>> dispatch(ServerHttpRequest request, String token, String secretKey) {
        logRequest(request, token, secretKey);
        return RequestBodyReader.read(request).flatMap(body ->
                secretKey != null
                        ? serverUseApiService.handle(secretKey, token, request, body)
                        : browserUseApiService.handle(token, request, body)
        );
    }

    private void logRequest(ServerHttpRequest request, String token, String secretKey) {
        if (!log.isDebugEnabled()) return;
        String path = request.getURI().getPath();
        String query = request.getURI().getQuery();
        String pathWithQuery = (query == null || query.isBlank()) ? path : path + "?" + query;
        log.debug("[Proxy][{}] {} {} token={} ip={} ua={}",
                secretKey != null ? "Server" : "Browser",
                request.getMethod(),
                pathWithQuery,
                TokenMasker.mask(token),
                request.getRemoteAddress(),
                request.getHeaders().getFirst("User-Agent"));
    }
}
