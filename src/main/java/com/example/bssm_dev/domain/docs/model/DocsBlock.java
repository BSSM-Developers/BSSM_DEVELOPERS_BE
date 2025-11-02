package com.example.bssm_dev.domain.docs.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class DocsBlock {
    private String id;
    private String module;
    private String content;
}
