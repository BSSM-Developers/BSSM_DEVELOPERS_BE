package com.example.bssm_dev.domain.github.controller;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.github.dto.request.RegisterGitHubRepoRequest;
import com.example.bssm_dev.domain.github.dto.response.GitHubBranchItem;
import com.example.bssm_dev.domain.github.dto.response.GitHubRepoItem;
import com.example.bssm_dev.domain.github.dto.response.RegisteredRepoResponse;
import com.example.bssm_dev.domain.github.service.GitHubRepositoryQueryService;
import com.example.bssm_dev.domain.github.service.GitHubRepositoryService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github/repositories")
@RequiredArgsConstructor
public class GitHubRepositoryController {

    private final GitHubRepositoryService gitHubRepositoryService;
    private final GitHubRepositoryQueryService gitHubRepositoryQueryService;

    /**
     * GitHub App이 설치된 레포지토리 목록 조회 (GitHub API 실시간 조회)
     */
    @GetMapping("/available")
    public ResponseEntity<ResponseDto<List<GitHubRepoItem>>> getAvailableRepositories(
            @CurrentUser User user
    ) {
        List<GitHubRepoItem> repos = gitHubRepositoryQueryService.getInstallationRepositories(user.getUserId());
        return ResponseEntity.ok(HttpUtil.success("available repositories", repos));
    }

    /**
     * 특정 레포지토리의 브랜치 목록 조회
     */
    @GetMapping("/{owner}/{repo}/branches")
    public ResponseEntity<ResponseDto<List<GitHubBranchItem>>> getBranches(
            @CurrentUser User user,
            @PathVariable String owner,
            @PathVariable String repo
    ) {
        List<GitHubBranchItem> branches = gitHubRepositoryQueryService.getBranches(user.getUserId(), owner, repo);
        return ResponseEntity.ok(HttpUtil.success("branches", branches));
    }

    /**
     * 등록된 레포지토리 목록 조회 (DB)
     */
    @GetMapping
    public ResponseEntity<ResponseDto<List<RegisteredRepoResponse>>> getRegisteredRepositories(
            @CurrentUser User user
    ) {
        List<RegisteredRepoResponse> repos = gitHubRepositoryQueryService.getRegisteredRepositories(user.getUserId());
        return ResponseEntity.ok(HttpUtil.success("registered repositories", repos));
    }

    /**
     * 레포지토리 등록 (App이 설치된 레포만 가능)
     */
    @PostMapping
    public ResponseEntity<ResponseDto<RegisteredRepoResponse>> register(
            @CurrentUser User user,
            @Valid @RequestBody RegisterGitHubRepoRequest request
    ) {
        RegisteredRepoResponse response = gitHubRepositoryService.register(user.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(HttpUtil.success("repository registered", response));
    }

    /**
     * 레포지토리 등록 해제
     */
    @DeleteMapping("/{repoId}")
    public ResponseEntity<ResponseDto<Void>> delete(
            @CurrentUser User user,
            @PathVariable Long repoId
    ) {
        gitHubRepositoryService.delete(user.getUserId(), repoId);
        return ResponseEntity.ok(HttpUtil.success("repository unregistered"));
    }
}
