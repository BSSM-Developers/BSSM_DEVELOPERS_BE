package com.example.bssm_dev.domain.signup.mapper;

import com.example.bssm_dev.domain.signup.dto.request.SignupRequest;
import com.example.bssm_dev.domain.signup.dto.response.SignupResponse;
import com.example.bssm_dev.domain.signup.model.SignupForm;
import org.springframework.stereotype.Component;

@Component
public class SignupRequestMapper {

    public SignupForm toSignupRequest(SignupRequest signupRequestDto) {
        return SignupForm.of(
                signupRequestDto.name(),
                signupRequestDto.email(),
                signupRequestDto.profile()
        );
    }

    public SignupResponse toSignupResponse(SignupForm signupForm) {
        return new SignupResponse(
                signupForm.getSignupRequestId(),
                signupForm.getName(),
                signupForm.getEmail(),
                signupForm.getProfile(),
                signupForm.getPurpose(),
                signupForm.getState()
        );
    }
}