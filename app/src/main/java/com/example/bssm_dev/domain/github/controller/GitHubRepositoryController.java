package com.example.bssm_dev.domain.github.controller;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.github.dto.response.GitHubBranchItem;
import com.example.bssm_dev.domain.github.dto.response.GitHubRepoItem;
import com.example.bssm_dev.domain.github.dto.response.ParsedEndpointResponse;
import com.example.bssm_dev.domain.github.dto.response.RegisteredRepoResponse;
import com.example.bssm_dev.domain.github.service.GitHubRepositoryQueryService;
import com.example.bssm_dev.domain.github.service.GitHubRepositoryService;
import com.example.bssm_dev.domain.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "GitHub Repository", description = "GitHub 레포지토리 관리 API")
@RestController
@RequestMapping("/github/repositories")
@RequiredArgsConstructor
public class GitHubRepositoryController {

    private final GitHubRepositoryService gitHubRepositoryService;
    private final GitHubRepositoryQueryService gitHubRepositoryQueryService;

    @Operation(
            summary = "GitHub App이 설치된 레포지토리 목록 조회",
            description = "GitHub App이 설치된 레포지토리 목록을 GitHub API에서 실시간으로 조회합니다. " +
                          "GitHub App 미설치 시 400 에러가 반환됩니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "레포지토리 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "GitHub App이 설치되지 않음"),
            @ApiResponse(responseCode = "404", description = "GitHub 연동 내역 없음")
    })
    @GetMapping("/available")
    public ResponseEntity<ResponseDto<List<GitHubRepoItem>>> getAvailableRepositories(
            @CurrentUser User user
    ) {
        List<GitHubRepoItem> repos = gitHubRepositoryQueryService.getInstallationRepositories(user.getUserId());
        return ResponseEntity.ok(HttpUtil.success("available repositories", repos));
    }

    @Operation(
            summary = "레포지토리 브랜치 목록 조회",
            description = "특정 레포지토리의 브랜치 목록을 GitHub API에서 실시간으로 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "브랜치 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "GitHub App이 설치되지 않음"),
            @ApiResponse(responseCode = "404", description = "GitHub 연동 내역 없음")
    })
    @GetMapping("/{owner}/{repo}/branches")
    public ResponseEntity<ResponseDto<List<GitHubBranchItem>>> getBranches(
            @CurrentUser User user,
            @Parameter(description = "레포지토리 소유자 (GitHub 사용자명 또는 조직명)", example = "BSSM-Developers")
            @PathVariable String owner,
            @Parameter(description = "레포지토리 이름", example = "BSSM_DEVELOPERS_BE")
            @PathVariable String repo
    ) {
        List<GitHubBranchItem> branches = gitHubRepositoryQueryService.getBranches(user.getUserId(), owner, repo);
        return ResponseEntity.ok(HttpUtil.success("branches", branches));
    }

    @Operation(
            summary = "등록된 레포지토리 목록 조회",
            description = "현재 사용자가 문서 생성을 통해 등록된 레포지토리 목록을 DB에서 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록된 레포지토리 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ResponseDto<List<RegisteredRepoResponse>>> getRegisteredRepositories(
            @CurrentUser User user
    ) {
        List<RegisteredRepoResponse> repos = gitHubRepositoryQueryService.getRegisteredRepositories(user.getUserId());
        return ResponseEntity.ok(HttpUtil.success("registered repositories", repos));
    }

    @Operation(
            summary = "레포지토리 등록 해제",
            description = "등록된 레포지토리를 해제합니다. 해제 후에는 해당 레포지토리의 push 이벤트를 더 이상 처리하지 않습니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "레포지토리 등록 해제 성공"),
            @ApiResponse(responseCode = "403", description = "본인 레포지토리가 아님"),
            @ApiResponse(responseCode = "404", description = "등록된 레포지토리를 찾을 수 없음")
    })
    @DeleteMapping("/{repoId}")
    public ResponseEntity<ResponseDto<Void>> delete(
            @CurrentUser User user,
            @Parameter(description = "등록된 레포지토리 ID", example = "1")
            @PathVariable Long repoId
    ) {
        gitHubRepositoryService.delete(user.getUserId(), repoId);
        return ResponseEntity.ok(HttpUtil.success("repository unregistered"));
    }

    @Operation(
            summary = "레포지토리 AST 파싱 엔드포인트 목록 조회",
            description = "GitHub 레포지토리를 AST로 실시간 분석해 엔드포인트 목록을 반환합니다. " +
                          "githubRepoId는 /github/repositories/available 응답의 id 필드를 사용하세요.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파싱된 엔드포인트 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "GitHub App 미설치"),
            @ApiResponse(responseCode = "404", description = "GitHub 연동 내역 없음")
    })
    @GetMapping("/{githubRepoId}/parsed-endpoints")
    public ResponseEntity<ResponseDto<List<ParsedEndpointResponse>>> getParsedEndpoints(
            @CurrentUser User user,
            @Parameter(description = "GitHub 레포지토리 ID (available 응답의 id)", example = "1132804732")
            @PathVariable Long githubRepoId,
            @Parameter(description = "브랜치명", example = "main")
            @RequestParam String repoFullName,
            @Parameter(description = "레포지토리 full_name (owner/repo)", example = "Denormalization/FE")
            @RequestParam String branch
    ) {
        List<ParsedEndpointResponse> endpoints = gitHubRepositoryQueryService.getParsedEndpoints(user.getUserId(), repoFullName, branch);
        return ResponseEntity.ok(HttpUtil.success("parsed endpoints", endpoints));
    }
}
