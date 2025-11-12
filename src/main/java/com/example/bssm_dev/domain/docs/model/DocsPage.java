package com.example.bssm_dev.domain.docs.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "docs_page")
@Builder
@Getter
public class DocsPage {
    @Id
    private String id;
    private String mappedId;
    private String docsId;
    private List<DocsPageBlock> docsBlocks;

    public void updateDocsBlocks(List<DocsPageBlock> docsBlocks) {
        this.docsBlocks = docsBlocks;
    }
}

