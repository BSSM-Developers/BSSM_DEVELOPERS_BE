package com.example.bssm_dev.domain.docs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class DocsPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docsPageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docs_section_id", nullable = false)
    private DocsSection docsSection;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "`order`")
    private Long order;

    @OneToOne(mappedBy = "docsPage", cascade = CascadeType.ALL, orphanRemoval = true)
    private ApiPage apiPage;

    public static DocsPage of(DocsSection docsSection, String title, String description, Long order) {
        return DocsPage.builder()
                .docsSection(docsSection)
                .title(title)
                .description(description)
                .order(order)
                .build();
    }
}
