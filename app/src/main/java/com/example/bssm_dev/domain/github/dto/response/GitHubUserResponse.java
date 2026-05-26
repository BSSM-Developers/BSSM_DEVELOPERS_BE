package com.example.bssm_dev.domain.github.dto.response;

public record GitHubUserResponse(
        Long id,
        String login,
        String name,
        String email,
        String avatar_url
) {}
