package com.example.bssm_dev.domain.github.repository;

import com.example.bssm_dev.domain.github.model.GitHubConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GitHubConnectionRepository extends JpaRepository<GitHubConnection, Long> {
    Optional<GitHubConnection> findByUserId(Long userId);
    Optional<GitHubConnection> findByGithubId(Long githubId);
    boolean existsByUserId(Long userId);
}
