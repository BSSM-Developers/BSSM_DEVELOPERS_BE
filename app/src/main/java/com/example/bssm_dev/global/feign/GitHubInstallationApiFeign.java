package com.example.bssm_dev.global.feign;

import com.example.bssm_dev.domain.github.dto.response.GitHubBranchItem;
import com.example.bssm_dev.domain.github.dto.response.GitHubRepoListApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "githubInstallationApi", url = "https://api.github.com")
public interface GitHubInstallationApiFeign {

    @GetMapping("/installation/repositories")
    GitHubRepoListApiResponse getInstallationRepositories(
            @RequestHeader("Authorization") String installationToken,
            @RequestHeader("Accept") String accept
    );

    @GetMapping("/repos/{owner}/{repo}/branches")
    List<GitHubBranchItem> getBranches(
            @RequestHeader("Authorization") String installationToken,
            @RequestHeader("Accept") String accept,
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page
    );
}
