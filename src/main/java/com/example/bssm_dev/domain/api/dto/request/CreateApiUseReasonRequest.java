package com.example.bssm_dev.domain.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateApiUseReasonRequest(
        @NotNull(message = "API ID는 필수입니다.")
        Long apiId,

        @NotBlank(message = "API 사용 사유는 필수입니다.")
        String apiUseReason
) {
}
