package com.example.bssm_dev.domain.auth.dto.response;


public record GoogleUserResponse (
        String name,
        String email,
        String profile
) {
}
