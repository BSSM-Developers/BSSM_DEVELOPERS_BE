package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DocsCreateRequest(
        @NotBlank(message = "문서 제목은 필수입니다.")
        String title,
        @NotBlank(message = "문서 설명은 필수입니다.")
        String description,
        @NotBlank(message = "도메인은 필수입니다.")
        String domain,
        @NotBlank(message = "깃허브 레포지토리 URL은 필수입니다.")
        String repositoryUrl,
        @NotNull(message = "자동 승인 여부는 필수입니다.")
        Boolean autoApproval,

        @NotNull(message = "sidebar는 null일 수 없습니다")
        @Valid
        CreateDocsSideBarRequest sidebar,

        @NotNull(message = "docs page는 null일 수 없습니다")
        @Valid
        List<CreateDocsPageRequest> docsPages
) {
}
