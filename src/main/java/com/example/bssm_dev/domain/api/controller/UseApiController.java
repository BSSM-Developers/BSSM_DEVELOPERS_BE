package com.example.bssm_dev.domain.api.controller;

import com.example.bssm_dev.domain.api.dto.response.ProxyResponse;
import com.example.bssm_dev.domain.api.service.UseApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proxy/**")
public class UseApiController {

    private final UseApiService useApiService;

    /**
     * 외부 API GET 요청 프록시
     */
    @GetMapping
    public ResponseEntity<Object> useApiByGet(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        Object response = useApiService.get(secretKey, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 외부 API POST 요청 프록시
     */
    @PostMapping
    public ResponseEntity<ProxyResponse> useApiByPost(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        ProxyResponse response = useApiService.post(secretKey, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 외부 API PATCH 요청 프록시
     */
    @PatchMapping
    public ResponseEntity<ProxyResponse> useApiByPatch(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        ProxyResponse response = useApiService.patch(secretKey, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 외부 API PUT 요청 프록시
     */
    @PutMapping
    public ResponseEntity<ProxyResponse> useApiByPut(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {

        ProxyResponse response = useApiService.put(secretKey, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 외부 API DELETE 요청 프록시
     */
    @DeleteMapping
    public ResponseEntity<ProxyResponse> useApiByDelete(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        ProxyResponse response = useApiService.delete(secretKey, token, request);
        return ResponseEntity.ok(response);
    }

}
