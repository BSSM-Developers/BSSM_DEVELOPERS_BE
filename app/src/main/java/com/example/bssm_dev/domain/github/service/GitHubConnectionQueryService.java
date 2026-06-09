package com.example.bssm_dev.domain.github.service;

import com.example.bssm_dev.domain.github.dto.response.GitHubConnectionResponse;
import com.example.bssm_dev.domain.github.exception.GitHubConnectionNotFoundException;
import com.example.bssm_dev.domain.github.model.GitHubConnection;
import com.example.bssm_dev.domain.github.repository.GitHubConnectionRepository;
import com.example.bssm_dev.domain.github.repository.GitHubInstallationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GitHubConnectionQueryService {

    private final GitHubConnectionRepository gitHubConnectionRepository;
    private final GitHubInstallationRepository gitHubInstallationRepository;

    @Transactional(readOnly = true)
    public GitHubConnectionResponse getConnection(Long userId) {
        GitHubConnection connection = gitHubConnectionRepository.findByUserId(userId)
                .orElseThrow(GitHubConnectionNotFoundException::raise);

        boolean appInstalled = gitHubInstallationRepository.existsByUserId(connection.getUserId());
        return new GitHubConnectionResponse(connection.getGithubLogin(), appInstalled);
    }

    @Transactional(readOnly = true)
    public boolean isConnected(Long userId) {
        return gitHubConnectionRepository.existsByUserId(userId);
    }
}
