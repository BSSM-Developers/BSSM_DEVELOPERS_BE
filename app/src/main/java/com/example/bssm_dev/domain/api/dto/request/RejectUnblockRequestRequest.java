package com.example.bssm_dev.domain.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RejectUnblockRequestRequest(
        @NotBlank(message = "거부 사유는 필수입니다.")
        String rejectReason
) {
}
