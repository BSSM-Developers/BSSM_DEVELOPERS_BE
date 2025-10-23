package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateOriginalDocsRequest(
        @NotBlank(message = "문서 제목은 필수입니다")
        String docsTitle,
        
        String docsDescription,
        String domain,
        String repositoryUrl,
        Boolean autoApproval,
        
        @NotNull(message = "섹션 목록은 null일 수 없습니다")
        @NotEmpty(message = "최소 하나의 섹션이 필요합니다")
        @Valid
        List<CreateDocsSectionRequest> docsSections
) {
}
