package com.example.bssm_dev.domain.api.controller.ProxyApi;

import com.example.bssm_dev.domain.api.service.ServerUseApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proxy-server/**")
public class ServerUseApiController {

    private final ServerUseApiService useApiService;

    /**
     * 서버용 외부 API GET 요청 프록시 (secretKey 기반 인증)
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
     * 서버용 외부 API POST 요청 프록시 (secretKey 기반 인증)
     */
    @PostMapping
    public ResponseEntity<Object> useApiByPost(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        Object response = useApiService.post(secretKey, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 서버용 외부 API PATCH 요청 프록시 (secretKey 기반 인증)
     */
    @PatchMapping
    public ResponseEntity<Object> useApiByPatch(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        Object response = useApiService.patch(secretKey, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 서버용 외부 API PUT 요청 프록시 (secretKey 기반 인증)
     */
    @PutMapping
    public ResponseEntity<Object> useApiByPut(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        Object response = useApiService.put(secretKey, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 서버용 외부 API DELETE 요청 프록시 (secretKey 기반 인증)
     */
    @DeleteMapping
    public ResponseEntity<Object> useApiByDelete(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        Object response = useApiService.delete(secretKey, token, request);
        return ResponseEntity.ok(response);
    }
}
