package com.example.bssm_dev.domain.docs.model;

import jakarta.persistence.Embedded;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "docs")
public class Docs {
    private String id;
    @Embedded
    private List<DocsBlock> docsBlocks;
}

