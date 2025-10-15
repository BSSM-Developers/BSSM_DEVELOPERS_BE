package com.example.bssm_dev.domain.docs.dto.response;

public record DocsPageResponse(
        Long docsPageId,
        String title,
        String description,
        Long order,
        String type,
        ApiDetailResponse apiDetail
) {
    public Long apiId() {
        return this.apiDetail.apiId();
    }
}
