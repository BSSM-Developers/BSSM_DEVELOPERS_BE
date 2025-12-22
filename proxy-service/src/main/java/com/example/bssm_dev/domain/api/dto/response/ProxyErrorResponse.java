package com.example.bssm_dev.domain.api.dto.response;

/**
 * 프록시 호출 실패 시 클라이언트가 실패 원인을 구분할 수 있도록 전달하는 DTO.
 */
public record ProxyErrorResponse(
        boolean upstreamError, // true면 외부 API 오류, false면 우리 서버 내부 오류
        int statusCode,
        String message,
        String body // 외부 API 응답 본문 또는 내부 오류 원인
) {
}
