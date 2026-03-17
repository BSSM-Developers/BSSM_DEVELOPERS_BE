package com.example.bssm_dev.domain.docs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "docs_page")
@TypeAlias("reference")
@SuperBuilder
@Getter
@NoArgsConstructor
public class ReferenceDocsPage extends DocsPage {
    private String sourceDocsId;
    private String sourceMappedId;
}
