package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.aspectj.weaver.ast.Not;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateOriginalDocsRequest(
        @NotBlank(message = "문서 제목은 필수입니다")
        String title,

        String description,
        String domain,
        String repositoryUrl,
        Boolean autoApproval,

        @NotNull(message = "sidebar는 null일 수 없습니다")
        @Valid
        CreateDocsSideBarRequest sidebar,

        @NotNull(message = "docs page는 null일 수 없습니다")
        @Valid
        List<CreateDocsPageRequest> docsPages
) {
}
