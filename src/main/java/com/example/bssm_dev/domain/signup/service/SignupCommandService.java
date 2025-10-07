package com.example.bssm_dev.domain.signup.service;

import com.example.bssm_dev.domain.auth.exception.SignupRequestAlreadyExistsException;
import com.example.bssm_dev.domain.signup.dto.request.SignupRequest;
import com.example.bssm_dev.domain.signup.dto.request.UpdatePurposeRequest;
import com.example.bssm_dev.domain.signup.exception.InvalidSignupTokenException;
import com.example.bssm_dev.domain.signup.exception.SignupRequestNotFoundException;
import com.example.bssm_dev.domain.signup.exception.UnauthorizedSignupAccessException;
import com.example.bssm_dev.domain.signup.mapper.SignupRequestMapper;
import com.example.bssm_dev.domain.signup.model.SignupForm;
import com.example.bssm_dev.domain.signup.model.SignupToken;
import com.example.bssm_dev.domain.signup.repository.SignupRequestRepository;
import com.example.bssm_dev.domain.signup.repository.SignupTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SignupCommandService {

    private final SignupRequestRepository signupRequestRepository;
    private final SignupTokenRepository signupTokenRepository;
    private final SignupRequestMapper signupRequestMapper;

    public String createSignupRequest(SignupRequest signupRequestDto) {
        boolean isSignupRequestExists = signupRequestRepository.existsByEmail(signupRequestDto.email());

        if (isSignupRequestExists) {
            throw SignupRequestAlreadyExistsException.raise();
        }

        SignupForm signupRequest = signupRequestMapper.toSignupRequest(signupRequestDto);
        signupRequestRepository.save(signupRequest);

        String token = UUID.randomUUID().toString();
        SignupToken signupToken = SignupToken.of(token, signupRequestDto.email());
        signupTokenRepository.save(signupToken);

        return token;
    }


    public void updatePurpose(Long signupRequestId, UpdatePurposeRequest request, String signupToken) {
        SignupToken token = signupTokenRepository.findById(signupToken)
                .orElseThrow(InvalidSignupTokenException::raise);

        SignupForm signupForm = signupRequestRepository.findById(signupRequestId)
                .orElseThrow(SignupRequestNotFoundException::raise);

        boolean equalsSignUpFormEmail = signupForm.getEmail().equals(token.getEmail());

        if (!equalsSignUpFormEmail) {
            throw UnauthorizedSignupAccessException.raise();
        }

        signupForm.updatePurpose(request.purpose());
    }
}
