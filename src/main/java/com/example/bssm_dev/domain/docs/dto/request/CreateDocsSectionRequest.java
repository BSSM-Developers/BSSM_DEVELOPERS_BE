package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateDocsSectionRequest(
        @NotBlank(message = "섹션 제목은 필수입니다")
        String docsSectionTitle,
        
        @NotNull(message = "페이지 목록은 null일 수 없습니다")
        @NotEmpty(message = "최소 하나의 페이지가 필요합니다")
        @Valid
        List<CreateDocsPageRequest> docsPages
) {
}
