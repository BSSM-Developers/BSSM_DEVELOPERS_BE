package com.example.bssm_dev.domain.docs.dto.response;

public record DocsListResponse(
        String docsId,
        String title,
        String description,
        Long writerId,
        String writer,
        String type,
        String repositoryUrl,
        Boolean autoApproval
) {
}
