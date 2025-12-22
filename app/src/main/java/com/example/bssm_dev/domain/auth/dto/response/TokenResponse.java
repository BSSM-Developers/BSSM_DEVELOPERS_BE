package com.example.bssm_dev.domain.auth.dto.response;

public record TokenResponse (
        String accessToken,
        String refreshToken
) {

}
