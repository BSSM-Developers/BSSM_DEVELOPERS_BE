package com.example.bssm_dev.domain.api.controller;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.domain.api.dto.response.ApiHealthCheckResponse;
import com.example.bssm_dev.domain.api.service.HealthCheckApiService;
import com.example.bssm_dev.domain.api.util.RequestBodyReader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/healthy")
public class HealthCheckApiController {
    private final HealthCheckApiService healthCheckApiService;

    @PostMapping
    public Mono<ResponseEntity<ResponseDto<ApiHealthCheckResponse>>> check(
            @RequestParam String endpoint,
            @RequestParam String method,
            @RequestParam String domain,
            ServerHttpRequest request
    ) {
        return RequestBodyReader.read(request)
                .flatMap(body -> healthCheckApiService.check(endpoint, method, domain, request, body))
                .map(result -> ResponseEntity.ok(new ResponseDto<>("health check ok", result)));
    }
}
