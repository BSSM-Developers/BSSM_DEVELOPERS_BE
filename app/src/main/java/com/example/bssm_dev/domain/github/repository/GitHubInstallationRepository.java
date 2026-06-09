package com.example.bssm_dev.domain.github.repository;

import com.example.bssm_dev.domain.github.model.GitHubInstallation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GitHubInstallationRepository extends JpaRepository<GitHubInstallation, Long> {
    List<GitHubInstallation> findAllByUserId(Long userId);
    Optional<GitHubInstallation> findByInstallationId(Long installationId);
    void deleteByInstallationId(Long installationId);
    boolean existsByUserId(Long userId);
}
