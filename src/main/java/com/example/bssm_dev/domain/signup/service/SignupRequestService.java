package com.example.bssm_dev.domain.signup.service;

import com.example.bssm_dev.domain.auth.exception.SignupRequestAlreadyExistsException;
import com.example.bssm_dev.domain.signup.mapper.SignupRequestMapper;
import com.example.bssm_dev.domain.signup.model.SignupForm;
import com.example.bssm_dev.domain.signup.repository.SignupRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupRequestService {

    private final SignupRequestRepository signupRequestRepository;
    private final SignupRequestMapper signupRequestMapper;

    @Transactional
    public void createSignupRequest(com.example.bssm_dev.domain.signup.dto.request.SignupRequest signupRequestDto) {
        boolean isSignupRequestExists = signupRequestRepository.existsByEmail(signupRequestDto.email());

        if (isSignupRequestExists) {
            throw SignupRequestAlreadyExistsException.raise();
        }

        SignupForm signupRequest = signupRequestMapper.toSignupRequest(signupRequestDto);
        signupRequestRepository.save(signupRequest);
    }
}