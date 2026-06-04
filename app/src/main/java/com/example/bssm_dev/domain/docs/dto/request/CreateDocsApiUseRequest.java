package com.example.bssm_dev.domain.docs.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateDocsApiUseRequest(
        @NotBlank(message = "API 사용 사유는 필수입니다.")
        String useReason
) {
}
