package com.example.bssm_dev.domain.docs.model;

import com.example.bssm_dev.domain.docs.model.type.DocsModule;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DocsPageBlock {
    private String id;
    private String mappedId;
    private DocsModule module;
    private String content;

    public String getModuleName() {
        return this.module.name().toLowerCase();
    }
}
