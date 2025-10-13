package com.example.bssm_dev.domain.docs.model;

import com.example.bssm_dev.domain.docs.model.type.DocsType;
import com.example.bssm_dev.domain.user.model.User;
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
public class Docs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocsType type;

    private String domain;

    private String repositoryUrl;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean autoApproval;

    @OneToMany(mappedBy = "docs", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 100)
    @Builder.Default
    private List<DocsSection> sections = new ArrayList<>();

    public void addSectionList(List<DocsSection> sectionList) {
        this.sections.addAll(sectionList);
    }

    public static Docs of(User creator, String title, String description, DocsType type, String domain, String repositoryUrl, Boolean autoApproval) {
        return Docs.builder()
                .creator(creator)
                .title(title)
                .description(description)
                .type(type)
                .domain(domain)
                .repositoryUrl(repositoryUrl)
                .autoApproval(autoApproval != null ? autoApproval : false)
                .build();
    }
}
