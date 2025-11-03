package com.example.bssm_dev.domain.docs.model.type;

public enum DocsModule {
    HEADLINE_1, HEADLINE_2, DOCS_1, LIST, CODE, IMAGE;

    public static DocsModule fromString(String module) {
        return DocsModule.valueOf(module.toUpperCase());
    }

}
