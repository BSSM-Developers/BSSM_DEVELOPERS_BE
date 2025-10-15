package com.example.bssm_dev.domain.docs.dto.response;

import java.util.List;

public record DocsDetailResponse(
        Long docsId,
        String title,
        String description,
        String type,
        String domain,
        String repositoryUrl,
        Boolean autoApproval,
        List<DocsSectionResponse> sections
) {
}
