package com.example.bssm_dev.domain.github.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class GitHubInstallation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private Long installationId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private GitHubInstallation(Long userId, Long installationId) {
        this.userId = userId;
        this.installationId = installationId;
        this.createdAt = LocalDateTime.now();
    }

    public static GitHubInstallation of(Long userId, Long installationId) {
        return new GitHubInstallation(userId, installationId);
    }
}
