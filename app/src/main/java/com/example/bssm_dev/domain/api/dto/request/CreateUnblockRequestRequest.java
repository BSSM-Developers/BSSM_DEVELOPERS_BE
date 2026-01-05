package com.example.bssm_dev.domain.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUnblockRequestRequest(
        @NotBlank(message = "차단 해제 요청 사유는 필수입니다.")
        String reason
) {
}
