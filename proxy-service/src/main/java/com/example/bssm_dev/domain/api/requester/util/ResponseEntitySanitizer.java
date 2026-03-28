package com.example.bssm_dev.domain.api.requester.util;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public final class ResponseEntitySanitizer {

    /**
     * 허용하는 Content-Type 목록.
     * 이 외의 타입은 악성 바이너리 또는 스크립트 전달 가능성이 있어 차단한다.
     */
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/json",
            "application/json; charset=utf-8",
            "text/plain",
            "text/plain; charset=utf-8",
            "text/xml",
            "application/xml",
            "application/x-www-form-urlencoded"
    );

    private ResponseEntitySanitizer() {
    }

    public static ResponseEntity<byte[]> sanitize(ResponseEntity<byte[]> entity) {
        validateContentType(entity.getHeaders());

        HttpHeaders filtered = new HttpHeaders();
        entity.getHeaders().forEach((name, values) -> {
            // hop-by-hop 헤더 및 CORS 헤더 제거
            if (HeaderFilterUtil.isHopByHopHeader(name) || HeaderFilterUtil.isExcludedResponseHeader(name)) {
                return;
            }
            // Content-Disposition 제거: 악성 파일 다운로드 유도 방지
            if ("content-disposition".equalsIgnoreCase(name)) {
                return;
            }
            filtered.put(name, values);
        });
        return new ResponseEntity<>(entity.getBody(), filtered, entity.getStatusCode());
    }

    private static void validateContentType(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType == null) {
            return;
        }
        // charset 등 파라미터를 제거한 기본 타입으로 비교
        String baseType = contentType.getType() + "/" + contentType.getSubtype();
        String baseTypeWithCharset = headers.getFirst(HttpHeaders.CONTENT_TYPE);

        boolean allowed = ALLOWED_CONTENT_TYPES.stream()
                .anyMatch(allowed_ ->
                        allowed_.equalsIgnoreCase(baseType)
                        || (baseTypeWithCharset != null && baseTypeWithCharset.toLowerCase().startsWith(allowed_.toLowerCase()))
                );

        if (!allowed) {
            throw new BlockedContentTypeException(contentType.toString());
        }
    }

    public static class BlockedContentTypeException extends GlobalException {
        public BlockedContentTypeException(String contentType) {
            super(ErrorCode.BLOCKED_CONTENT_TYPE);
        }
    }
}
