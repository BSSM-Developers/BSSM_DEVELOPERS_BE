package com.example.bssm_dev.domain.api.controller;

import com.example.bssm_dev.domain.api.service.BrowserUseApiService;
import com.example.bssm_dev.domain.api.service.ServerUseApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.buffer.DataBufferUtils;
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
    public Mono<ResponseEntity<byte[]>> useApiByGet(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request).flatMap(body ->
                secretKey != null
                        ? serverUseApiService.get(secretKey, token, request, body)
                        : browserUseApiService.get(token, request, body)
        );
    }

    @PostMapping("/**")
    public Mono<ResponseEntity<byte[]>> useApiByPost(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request).flatMap(body ->
                secretKey != null
                        ? serverUseApiService.post(secretKey, token, request, body)
                        : browserUseApiService.post(token, request, body)
        );
    }

    @PatchMapping("/**")
    public Mono<ResponseEntity<byte[]>> useApiByPatch(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request).flatMap(body ->
                secretKey != null
                        ? serverUseApiService.patch(secretKey, token, request, body)
                        : browserUseApiService.patch(token, request, body)
        );
    }

    @PutMapping("/**")
    public Mono<ResponseEntity<byte[]>> useApiByPut(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request).flatMap(body ->
                secretKey != null
                        ? serverUseApiService.put(secretKey, token, request, body)
                        : browserUseApiService.put(token, request, body)
        );
    }

    @DeleteMapping("/**")
    public Mono<ResponseEntity<byte[]>> useApiByDelete(
            ServerHttpRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader(value = "bssm-dev-secret", required = false) String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return readBody(request).flatMap(body ->
                secretKey != null
                        ? serverUseApiService.delete(secretKey, token, request, body)
                        : browserUseApiService.delete(token, request, body)
        );
    }

    private void logProxyRequest(ServerHttpRequest request, String token, String secretKey) {
        String path = request.getURI().getPath();
        String query = request.getURI().getQuery();
        String pathWithQuery = (query == null || query.isBlank()) ? path : path + "?" + query;
        String mode = secretKey != null ? "Server" : "Browser";

        log.debug(
                "[Proxy][{}] {} {} token={} ip={} ua={}",
                mode,
                request.getMethod(),
                pathWithQuery,
                mask(token),
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
        return value.substring(0, 3) + "***" + value.substring(value.length() - 3);
    }
}
