package com.example.bssm_dev.domain.user.dto.request;

import com.example.bssm_dev.domain.auth.dto.response.GoogleUserResponse;

public record UserRequest (
        String name,
        String email,
        String profile
) {
    public static UserRequest fromGoogleUser(GoogleUserResponse googleUser) {
        return new UserRequest(googleUser.name(), googleUser.email(), googleUser.profile());
    }
}
