package com.example.bssm_dev.domain.user.dto.response;

public record UserLoginResponse (
        Long userId,
        String email,
        String role
) {
}
