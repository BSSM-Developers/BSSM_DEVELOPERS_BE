package com.example.bssm_dev.domain.api.controller;

import com.example.bssm_dev.domain.api.dto.response.ProxyResponse;
import com.example.bssm_dev.domain.api.service.UseApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proxy/**")
@Slf4j
public class UseApiController {

    private final UseApiService useApiService;

    @GetMapping
    public ResponseEntity<ProxyResponse> useApiByGet(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        String endpoint = extractEndpoint(request);

        ProxyResponse response = useApiService.get(secretKey, token, endpoint);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProxyResponse> useApiByPost(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey,
            @RequestBody(required = false) Object body
    ) {
        String endpoint = extractEndpoint(request);
        ProxyResponse response = useApiService.post(secretKey, token, endpoint, body);
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<ProxyResponse> useApiByPatch(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey,
            @RequestBody(required = false) Object body
    ) {
        String endpoint = extractEndpoint(request);

        ProxyResponse response = useApiService.patch(secretKey, token, endpoint, body);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ProxyResponse> useApiByPut(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey,
            @RequestBody(required = false) Object body
    ) {
        String endpoint = extractEndpoint(request);

        ProxyResponse response = useApiService.put(secretKey, token, endpoint, body);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<ProxyResponse> useApiByDelete(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        String endpoint = extractEndpoint(request);

        ProxyResponse response = useApiService.delete(secretKey, token, endpoint);
        return ResponseEntity.ok(response);
    }

    private static String extractEndpoint(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String endpoint = requestUri.substring("/api/proxy".length());
        log.info("요청 URI = {}", requestUri);
        log.info("endpoint = {}", endpoint);
        return endpoint;
    }
}
