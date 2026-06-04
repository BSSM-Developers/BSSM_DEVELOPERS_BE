package com.example.bssm_dev.global.feign;

import com.example.bssm_dev.domain.github.dto.response.GitHubUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "githubUser", url = "https://api.github.com")
public interface GitHubUserFeign {

    @GetMapping("/user")
    GitHubUserResponse getUser(@RequestHeader("Authorization") String authorization);
}
