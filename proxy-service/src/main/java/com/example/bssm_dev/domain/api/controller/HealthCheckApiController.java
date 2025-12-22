package com.example.bssm_dev.domain.api.controller;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.domain.api.dto.response.ApiHealthCheckResponse;
import com.example.bssm_dev.domain.api.service.HealthCheckApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/healthy")
public class HealthCheckApiController {
    private final HealthCheckApiService healthCheckApiService;

    /**
     * API 헬스체크 with Endpoint, Method
     */
    @PostMapping
    public Mono<ResponseEntity<ResponseDto<ApiHealthCheckResponse>>> healthCheckWithApiId(
            @RequestParam String endpoint,
            @RequestParam String method,
            @RequestParam String domain,
            ServerHttpRequest request
    ) {
        return readBody(request)
                .flatMap(body -> healthCheckApiService.check(endpoint, method, domain, request, body))
                .map(healthCheckResponse -> {
                    ResponseDto<ApiHealthCheckResponse> responseDto = new ResponseDto<>("heath check ok", healthCheckResponse);
                    return ResponseEntity.ok(responseDto);
                });
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
}
