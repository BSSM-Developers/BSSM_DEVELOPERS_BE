package com.example.bssm_dev.domain.docs.dto.request;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomDocsRequest (
        @NotBlank(message = "문서 제목은 필수입니다")
        String docsTitle,
        String docsDescription,
        String domain,
        String repositoryUrl
) {
}

