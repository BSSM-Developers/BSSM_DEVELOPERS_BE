package com.example.bssm_dev.domain.docs.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateDocsPageRequest (
        @NotBlank(message = "문서 페이지 ID는 필수입니다.")
        String id,
        List<DocsPageBlockRequest> blocks,
        String endpoint,      // original docs API 페이지인 경우에만 존재
        String method,        // original docs API 페이지인 경우에만 존재 (endpoint와 함께 사용)
        String sourceDocsId,  // 커스텀 docs API 참조 페이지인 경우에만 존재
        String sourceMappedId // 커스텀 docs API 참조 페이지인 경우에만 존재
) {
}
