package com.example.bssm_dev.domain.docs.dto.request;
import java.util.List;

public record CreateDocsPageRequest (
        String id,
        List<DocsPageBlockRequest> blocks
) {
}
