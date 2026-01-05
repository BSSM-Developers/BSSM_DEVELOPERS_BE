package com.example.bssm_dev.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long userId,
        String name,
        String email,
        String profile,
        String role
) {
}
