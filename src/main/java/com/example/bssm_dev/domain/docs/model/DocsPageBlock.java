package com.example.bssm_dev.domain.docs.model;

import lombok.Builder;

@Builder
public class DocsPageBlock {
    private String id;
    private String mappedId;
    private String module;
    private String content;
}
