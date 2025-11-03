package com.example.bssm_dev.domain.docs.model;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "docs")
public class Docs {
    private String id;
    private String title;
    private boolean auto_approval;
    private String description;
    private String repositoryUrl;
    private DocumentType type;
    private String domain;
}