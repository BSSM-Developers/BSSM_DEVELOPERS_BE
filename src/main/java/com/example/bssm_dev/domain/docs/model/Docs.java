package com.example.bssm_dev.domain.docs.model;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "docs")
@Builder
@Getter
public class Docs {
    @Id
    private String id;
    private String title;
    private boolean auto_approval;
    private String description;
    private String repositoryUrl;
    private DocumentType type;
    private String domain;
    private Long writerId;
}