package com.example.bssm_dev.domain.github.repository;

import com.example.bssm_dev.domain.github.model.GitHubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GitHubRepositoryRepository extends JpaRepository<GitHubRepository, Long> {
    List<GitHubRepository> findAllByUserId(Long userId);
    Optional<GitHubRepository> findByIdAndUserId(Long id, Long userId);
    Optional<GitHubRepository> findByUserIdAndRepoFullNameAndBranch(Long userId, String repoFullName, String branch);
    boolean existsByUserIdAndRepoFullNameAndBranch(Long userId, String repoFullName, String branch);
    List<GitHubRepository> findAllByRepoFullNameAndBranch(String repoFullName, String branch);
}
