package com.example.bssm_dev.domain.signup.dto.response;

import com.example.bssm_dev.domain.signup.model.type.SignUpFormState;

public record SignupResponse(
        Long signupRequestId,
        String name,
        String email,
        String profile,
        String purpose,
        SignUpFormState state
) {
}
