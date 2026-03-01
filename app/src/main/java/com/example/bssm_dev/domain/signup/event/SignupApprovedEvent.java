package com.example.bssm_dev.domain.signup.event;

public record SignupApprovedEvent(
        String email,
        String name,
        String profile
) {
}
