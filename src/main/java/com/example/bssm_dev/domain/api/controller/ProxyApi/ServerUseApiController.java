package com.example.bssm_dev.domain.api.controller.ProxyApi;

import com.example.bssm_dev.domain.api.service.ServerUseApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/proxy-server/**")
public class ServerUseApiController {

    private final ServerUseApiService serverUseApiService;

    /**
     * 서버용 외부 API GET 요청 프록시 (secretKey 기반 인증)
     */
    @GetMapping
    public Mono<ResponseEntity<byte[]>> useApiByGet(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return serverUseApiService.get(secretKey, token, request);
    }

    /**
     * 서버용 외부 API POST 요청 프록시 (secretKey 기반 인증)
     */
    @PostMapping
    public Mono<ResponseEntity<byte[]>> useApiByPost(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return serverUseApiService.post(secretKey, token, request);
    }

    /**
     * 서버용 외부 API PATCH 요청 프록시 (secretKey 기반 인증)
     */
    @PatchMapping
    public Mono<ResponseEntity<byte[]>> useApiByPatch(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return serverUseApiService.patch(secretKey, token, request);
    }

    /**
     * 서버용 외부 API PUT 요청 프록시 (secretKey 기반 인증)
     */
    @PutMapping
    public Mono<ResponseEntity<byte[]>> useApiByPut(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return serverUseApiService.put(secretKey, token, request);
    }

    /**
     * 서버용 외부 API DELETE 요청 프록시 (secretKey 기반 인증)
     */
    @DeleteMapping
    public Mono<ResponseEntity<byte[]>> useApiByDelete(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token,
            @RequestHeader("bssm-dev-secret") String secretKey
    ) {
        logProxyRequest(request, token, secretKey);
        return serverUseApiService.delete(secretKey, token, request);
    }

    private void logProxyRequest(HttpServletRequest request, String token, String secretKey) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String pathWithQuery = (query == null || query.isBlank()) ? path : path + "?" + query;

        log.info(
                "[Proxy][Server] {} {} token={} secret={} ip={} ua={}",
                request.getMethod(),
                pathWithQuery,
                mask(token),
                mask(secretKey),
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
        );
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
