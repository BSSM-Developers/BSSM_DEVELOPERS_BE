package com.example.bssm_dev.domain.docs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "docs_page")
@TypeAlias("content")
@SuperBuilder
@Getter
@NoArgsConstructor
public class ContentDocsPage extends DocsPage {
    private List<DocsPageBlock> docsBlocks;
    private String endpoint;
    private String method;

    public void updateDocsBlocks(List<DocsPageBlock> docsBlocks) {
        this.docsBlocks = docsBlocks;
    }
}
