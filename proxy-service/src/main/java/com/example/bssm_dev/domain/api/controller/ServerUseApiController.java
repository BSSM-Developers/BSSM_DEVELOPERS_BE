package com.example.bssm_dev.domain.api.controller;

import com.example.bssm_dev.domain.api.service.ServerUseApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/proxy-server/**")
public class ServerUseApiController {

    private final ServerUseApiService serverUseApiService;

    /**
     * 서버용 외부 API GET 요청 프록시 (secretKey 기반 인증)
     */
    @GetMapping
    public Mono<ResponseEntity<byte[]>> useApiByGet(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request)
                .flatMap(body -> serverUseApiService.get(secretKey, token, request, body));
    }

    /**
     * 서버용 외부 API POST 요청 프록시 (secretKey 기반 인증)
     */
    @PostMapping
    public Mono<ResponseEntity<byte[]>> useApiByPost(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request)
                .flatMap(body -> serverUseApiService.post(secretKey, token, request, body));
    }

    /**
     * 서버용 외부 API PATCH 요청 프록시 (secretKey 기반 인증)
     */
    @PatchMapping
    public Mono<ResponseEntity<byte[]>> useApiByPatch(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request)
                .flatMap(body -> serverUseApiService.patch(secretKey, token, request, body));
    }

    /**
     * 서버용 외부 API PUT 요청 프록시 (secretKey 기반 인증)
     */
    @PutMapping
    public Mono<ResponseEntity<byte[]>> useApiByPut(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request)
                .flatMap(body -> serverUseApiService.put(secretKey, token, request, body));
    }

    /**
     * 서버용 외부 API DELETE 요청 프록시 (secretKey 기반 인증)
     */
    @DeleteMapping
    public Mono<ResponseEntity<byte[]>> useApiByDelete(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request)
                .flatMap(body -> serverUseApiService.delete(secretKey, token, request, body));
    }

    private void logProxyRequest(ServerHttpRequest request, String token, String secretKey) {
        String path = request.getURI().getPath();
        String query = request.getURI().getQuery();
        String pathWithQuery = (query == null || query.isBlank()) ? path : path + "?" + query;

        log.debug(
                "[Proxy][Server] {} {} token={} secret={} ip={} ua={}",
                request.getMethod(),
                pathWithQuery,
                mask(token),
                mask(secretKey),
                request.getRemoteAddress(),
                request.getHeaders().getFirst("User-Agent")
        );
    }

    private Mono<byte[]> readBody(ServerHttpRequest request) {
        return DataBufferUtils.join(request.getBody())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .defaultIfEmpty(new byte[0]);
    }

    private String mask(String value) {
        if (value == null || value.isBlank()) {
            return "null";
        }
        if (value.length() <= 6) {
            return "***";
        }
        String prefix = value.substring(0, 3);
        String suffix = value.substring(value.length() - 3);
        return prefix + "***" + suffix;
    }
}
