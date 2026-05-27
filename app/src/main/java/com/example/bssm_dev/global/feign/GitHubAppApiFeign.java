package com.example.bssm_dev.global.feign;

import com.example.bssm_dev.domain.github.dto.response.GitHubInstallationTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "githubAppApi", url = "https://api.github.com")
public interface GitHubAppApiFeign {

    @PostMapping("/app/installations/{installationId}/access_tokens")
    GitHubInstallationTokenResponse getInstallationToken(
            @RequestHeader("Authorization") String appJwt,
            @RequestHeader("Accept") String accept,
            @PathVariable Long installationId
    );
}
