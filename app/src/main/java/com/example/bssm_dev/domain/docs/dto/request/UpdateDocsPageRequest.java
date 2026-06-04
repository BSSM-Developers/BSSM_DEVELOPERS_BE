package com.example.bssm_dev.domain.docs.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateDocsPageRequest(
        @NotNull(message = "문서 블록 리스트는 필수입니다.")
        @Valid
        List<DocsPageBlockRequest> docsBlocks
) {
}
