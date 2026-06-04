package com.example.bssm_dev.domain.github.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class GitHubRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long installationId;

    @Column(nullable = false)
    private String repoFullName;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private GitHubRepository(Long userId, Long installationId, String repoFullName, String branch) {
        this.userId = userId;
        this.installationId = installationId;
        this.repoFullName = repoFullName;
        this.branch = branch;
        this.createdAt = LocalDateTime.now();
    }

    public static GitHubRepository of(Long userId, Long installationId, String repoFullName, String branch) {
        return new GitHubRepository(userId, installationId, repoFullName, branch);
    }

    public boolean isOwnedBy(Long userId) {
        return this.userId.equals(userId);
    }

    public String toRepositoryUrl() {
        return "https://github.com/" + repoFullName;
    }
}
