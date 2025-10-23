package com.example.bssm_dev.domain.docs.dto.response;

import java.util.List;

public record DocsSectionResponse(
        Long docsSectionId,
        String title,
        Integer order,
        List<DocsPageResponse> pages
) {
}
