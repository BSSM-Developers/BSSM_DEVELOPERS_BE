package com.example.bssm_dev.domain.api.controller.ProxyApi;

import com.example.bssm_dev.domain.api.service.BrowserUseApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/proxy-browser/**")
public class BrowserUseApiController {

    private final BrowserUseApiService browserUseApiService;

    /**
     * 브라우저용 외부 API GET 요청 프록시 (도메인 기반 인증)
     */
    @GetMapping
    public Mono<ResponseEntity<Object>> useApiByGet(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        logProxyRequest(request, token);
        return browserUseApiService.get(token, request)
                .map(ResponseEntity::ok);
    }

    /**
     * 브라우저용 외부 API POST 요청 프록시 (도메인 기반 인증)
     */
    @PostMapping
    public Mono<ResponseEntity<Object>> useApiByPost(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        logProxyRequest(request, token);
        return browserUseApiService.post(token, request)
                .map(ResponseEntity::ok);
    }

    /**
     * 브라우저용 외부 API PATCH 요청 프록시 (도메인 기반 인증)
     */
    @PatchMapping
    public Mono<ResponseEntity<Object>> useApiByPatch(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        logProxyRequest(request, token);
        return browserUseApiService.patch(token, request)
                .map(ResponseEntity::ok);
    }

    /**
     * 브라우저용 외부 API PUT 요청 프록시 (도메인 기반 인증)
     */
    @PutMapping
    public Mono<ResponseEntity<Object>> useApiByPut(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        logProxyRequest(request, token);
        return browserUseApiService.put(token, request)
                .map(ResponseEntity::ok);
    }

    /**
     * 브라우저용 외부 API DELETE 요청 프록시 (도메인 기반 인증)
     */
    @DeleteMapping
    public Mono<ResponseEntity<Object>> useApiByDelete(
            HttpServletRequest request,
            @RequestHeader("bssm-dev-token") String token
    ) {
        logProxyRequest(request, token);
        return browserUseApiService.delete(token, request)
                .map(ResponseEntity::ok);
    }

    private void logProxyRequest(HttpServletRequest request, String token) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String pathWithQuery = (query == null || query.isBlank()) ? path : path + "?" + query;

        log.info(
                "[Proxy][Browser] {} {} token={} ip={} ua={}",
                request.getMethod(),
                pathWithQuery,
                mask(token),
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
