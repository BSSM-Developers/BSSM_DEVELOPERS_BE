package com.example.bssm_dev.domain.signup.mapper;

import com.example.bssm_dev.domain.signup.model.SignupForm;
import org.springframework.stereotype.Component;

@Component
public class SignupRequestMapper {

    public SignupForm toSignupRequest(com.example.bssm_dev.domain.signup.dto.request.SignupRequest signupRequestDto) {
        return SignupForm.of(
                signupRequestDto.name(),
                signupRequestDto.email(),
                signupRequestDto.profile()
        );
    }
}