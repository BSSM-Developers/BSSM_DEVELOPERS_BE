package com.example.bssm_dev.domain.docs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "docs_page")
@SuperBuilder
@Getter
@NoArgsConstructor
public abstract class DocsPage {
    @Id
    private String id;
    private String mappedId;
    private String docsId;
}
