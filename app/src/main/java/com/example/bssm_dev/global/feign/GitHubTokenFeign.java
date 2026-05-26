package com.example.bssm_dev.global.feign;

import com.example.bssm_dev.domain.github.dto.response.GitHubTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "githubToken", url = "https://github.com")
public interface GitHubTokenFeign {

    @PostMapping(value = "/login/oauth/access_token",
                 consumes = "application/x-www-form-urlencoded")
    GitHubTokenResponse getToken(
            @RequestHeader("Accept") String accept,
            @RequestBody String body
    );
}
