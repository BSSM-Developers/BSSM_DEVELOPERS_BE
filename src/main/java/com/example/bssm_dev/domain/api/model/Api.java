package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.docs.model.ApiPage;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Api {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false)
    private String endpoint;

    @Column(length = 15, nullable = false)
    private String method;

    @Column(length = 30, nullable = false)
    private String name;

    private String domain;

    private String repositoryUrl;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean autoApproval;

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ApiPage> apiPages = new ArrayList<>();

    public static Api of(User creator, String endpoint, String method, String name, String domain, String repositoryUrl, Boolean autoApproval) {
        return Api.builder()
                .creator(creator)
                .endpoint(endpoint)
                .method(method)
                .name(name)
                .domain(domain)
                .repositoryUrl(repositoryUrl)
                .autoApproval(autoApproval != null ? autoApproval : false)
                .build();
    }
}
