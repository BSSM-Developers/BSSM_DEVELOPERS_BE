package com.example.bssm_dev.domain.docs.model;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "docs")
public class Docs {
    private String id;
    private List<DocsBlock> docsBlocks;
}

