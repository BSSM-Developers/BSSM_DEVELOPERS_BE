package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateDocsPageRequest(
        @NotBlank(message = "페이지 제목은 필수입니다")
        String docsPageTitle,
        
        String docsPageDescription
) {
}
