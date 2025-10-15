package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateDocsPageRequest(
        @NotBlank(message = "페이지 제목은 필수입니다")
        String docsPageTitle,
        
        String docsPageDescription,
        
        @NotBlank(message = "페이지 타입은 필수입니다")
        String type, // "markdown" or "api"
        
        String method, // nullable - only for type "api"
        String endpoint, // nullable - only for type "api"
        
        @Valid
        ApiRequestData request, // nullable - only for type "api"
        
        @Valid
        ApiResponseData response // nullable - only for type "api"
) {
}
