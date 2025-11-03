package com.example.bssm_dev.domain.docs.model;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "docs_page")
public class DocsPage {
    private String id;
    private String mappedId;
    private List<DocsPageBlock> docsBlocks;
}

