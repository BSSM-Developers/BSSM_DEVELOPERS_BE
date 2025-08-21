package com.example.bssm_dev.domain.auth.dto.response;


public record GoogleUserResponse (
        String picture,
        String email,
        String profile
) {
}
