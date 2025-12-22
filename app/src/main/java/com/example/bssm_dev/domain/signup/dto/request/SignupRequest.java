package com.example.bssm_dev.domain.signup.dto.request;

public record SignupRequest(
        String name,
        String email,
        String profile
) {
}