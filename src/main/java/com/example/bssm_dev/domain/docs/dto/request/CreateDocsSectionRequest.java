package com.example.bssm_dev.domain.docs.dto.request;

import java.util.List;

public record CreateDocsSectionRequest(
        String docsSectionTitle,
        List<CreateDocsPageRequest> docsPages
) {
}
