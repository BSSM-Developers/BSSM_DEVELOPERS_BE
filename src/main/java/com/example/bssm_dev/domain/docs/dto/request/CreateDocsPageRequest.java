package com.example.bssm_dev.domain.docs.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateDocsPageRequest (
        @NotBlank(message = "문서 페이지 ID는 필수입니다.")
        String id,
        @NotNull(message = "문서 블록 리스트는 필수입니다.")
        List<DocsPageBlockRequest> blocks
) {
}
