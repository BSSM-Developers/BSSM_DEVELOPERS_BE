package com.example.bssm_dev.domain.github.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class GitHubConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private Long githubId;

    @Column(nullable = false)
    private String githubLogin;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    private Long installationId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private GitHubConnection(Long userId, Long githubId, String githubLogin,
                              String accessToken, String refreshToken) {
        this.userId = userId;
        this.githubId = githubId;
        this.githubLogin = githubLogin;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static GitHubConnection of(Long userId, Long githubId, String githubLogin,
                                       String accessToken, String refreshToken) {
        return new GitHubConnection(userId, githubId, githubLogin, accessToken, refreshToken);
    }

    public void updateTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void updateInstallationId(Long installationId) {
        this.installationId = installationId;
    }

    public void removeInstallation() {
        this.installationId = null;
    }
}
