package com.example.bssm_dev.domain.signup.mapper;

import com.example.bssm_dev.domain.signup.model.SignupRequest;
import org.springframework.stereotype.Component;

@Component
public class SignupRequestMapper {

    public SignupRequest toSignupRequest(com.example.bssm_dev.domain.signup.dto.request.SignupRequest signupRequestDto) {
        return SignupRequest.of(
                signupRequestDto.name(),
                signupRequestDto.email(),
                signupRequestDto.profile()
        );
    }
}