package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Api {
    @Id
    @Column(length = 500)
    private String apiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_group_id", nullable = false)
    private ApiGroup apiGroup;

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

    @Column(nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private Boolean isCurrent = true;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private Long githubRepositoryId;

    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ApiUseReason> apiUseReason = new ArrayList<>();

    public static Api createNew(String apiId, ApiGroup apiGroup, User creator, String endpoint, String method, String name, String domain, String repositoryUrl, Boolean autoApproval) {
        return Api.builder()
                .apiId(apiId)
                .apiGroup(apiGroup)
                .creator(creator)
                .endpoint(endpoint)
                .method(method)
                .name(name)
                .domain(domain)
                .repositoryUrl(repositoryUrl)
                .autoApproval(autoApproval != null ? autoApproval : false)
                .version(1)
                .isCurrent(true)
                .validFrom(LocalDateTime.now())
                .validTo(null)
                .build();
    }

    public Api nextVersion(String newApiId) {
        this.validTo = LocalDateTime.now();
        this.isCurrent = false;
        return Api.builder()
                .apiId(newApiId)
                .apiGroup(this.apiGroup)
                .creator(this.creator)
                .endpoint(this.endpoint)
                .method(this.method)
                .name(this.name)
                .domain(this.domain)
                .repositoryUrl(this.repositoryUrl)
                .autoApproval(this.autoApproval)
                .version(this.version + 1)
                .isCurrent(true)
                .validFrom(LocalDateTime.now())
                .validTo(null)
                .githubRepositoryId(this.githubRepositoryId)
                .build();
    }

    public void updateApiInfo(String endpoint, String method, String name, String domain) {
        this.endpoint = endpoint;
        this.method = method;
        this.name = name;
        this.domain = domain;
    }

    public boolean isCreator(User user) {
        return this.creator.equals(user);
    }
}
