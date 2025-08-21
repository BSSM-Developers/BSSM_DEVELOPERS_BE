package com.example.bssm_dev.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserLoginResponse (
        Long userId,
        String email,
        String role
) {
}
