package com.example.bssm_dev.domain.docs.dto.request;

public record DocsPageBlockRequest (
        String id,
        String module,
        String content
) {
}