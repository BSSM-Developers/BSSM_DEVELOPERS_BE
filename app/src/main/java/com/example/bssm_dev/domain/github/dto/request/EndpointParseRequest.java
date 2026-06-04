package com.example.bssm_dev.domain.github.dto.request;

public record EndpointParseRequest(
        String repoFullName,
        String branch,
        String installationToken
) {}
