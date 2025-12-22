package com.example.bssm_dev.domain.docs.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DocsPageBlockRequest (
        @NotBlank(message = "블록 ID는 필수입니다.")
        String id,
        @NotBlank(message = "모듈 이름은 필수입니다.")
        String module,
        @NotBlank(message = "콘텐츠 내용은 필수입니다.")
        String content
) {
}