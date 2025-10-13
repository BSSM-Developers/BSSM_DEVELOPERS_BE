package com.example.bssm_dev.domain.docs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class DocsSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docsSectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docs_id", nullable = false)
    private Docs docs;

    @Column(nullable = false)
    private String title;

    @Column(name = "`order`")
    private Long order;

    @OneToMany(mappedBy = "docsSection", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 100)
    private List<DocsPage> pages = new ArrayList<>();

    public void addPageList(List<DocsPage> page) {
        this.pages.addAll(page);
    }


    public static DocsSection of(Docs docs, String title, Long order) {
        return DocsSection.builder()
                .docs(docs)
                .title(title)
                .order(order)
                .build();
    }
}
