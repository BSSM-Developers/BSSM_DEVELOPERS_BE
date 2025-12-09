package com.example.bssm_dev.domain.api.controller.ProxyApi;

import com.example.bssm_dev.domain.api.service.UseApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proxy-browser/**")
public class BrowserUseApiController {

    private final UseApiService useApiService;

    /**
     * 브라우저용 외부 API GET 요청 프록시 (도메인 기반 인증)
     */
    @GetMapping
    public ResponseEntity<Object> useApiByGet(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        Object response = useApiService.get(null, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 브라우저용 외부 API POST 요청 프록시 (도메인 기반 인증)
     */
    @PostMapping
    public ResponseEntity<Object> useApiByPost(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        Object response = useApiService.post(null, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 브라우저용 외부 API PATCH 요청 프록시 (도메인 기반 인증)
     */
    @PatchMapping
    public ResponseEntity<Object> useApiByPatch(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        Object response = useApiService.patch(null, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 브라우저용 외부 API PUT 요청 프록시 (도메인 기반 인증)
     */
    @PutMapping
    public ResponseEntity<Object> useApiByPut(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        Object response = useApiService.put(null, token, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 브라우저용 외부 API DELETE 요청 프록시 (도메인 기반 인증)
     */
    @DeleteMapping
    public ResponseEntity<Object> useApiByDelete(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        Object response = useApiService.delete(null, token, request);
        return ResponseEntity.ok(response);
    }
}
