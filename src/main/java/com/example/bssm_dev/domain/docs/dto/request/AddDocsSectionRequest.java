package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddDocsSectionRequest(
        @NotBlank(message = "섹션 제목은 필수입니다")
        String docsSectionTitle,

        @Positive(message = "순서는 양수여야 합니다")
        int order
) {
}
