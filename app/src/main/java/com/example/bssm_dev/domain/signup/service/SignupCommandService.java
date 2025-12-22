package com.example.bssm_dev.domain.signup.service;

import com.example.bssm_dev.domain.signup.dto.request.SignupRequest;
import com.example.bssm_dev.domain.signup.dto.request.UpdatePurposeRequest;
import com.example.bssm_dev.domain.signup.exception.InvalidSignupTokenException;
import com.example.bssm_dev.domain.signup.exception.SignupRequestNotFoundException;
import com.example.bssm_dev.domain.signup.exception.UnauthorizedSignupAccessException;
import com.example.bssm_dev.domain.signup.mapper.SignupFormMapper;
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
@Transactional("transactionManager")
public class SignupCommandService {

    private final SignupRequestRepository signupRequestRepository;
    private final SignupTokenRepository signupTokenRepository;
    private final SignupFormMapper signupRequestMapper;

    public String createSignupRequest(SignupRequest signupRequestDto) {
        boolean isSignupRequestExists = signupRequestRepository.existsByEmail(signupRequestDto.email());

        if (!isSignupRequestExists) {
            SignupForm signupRequest = signupRequestMapper.toSignupRequest(signupRequestDto);
            signupRequestRepository.save(signupRequest);
        }

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

    public void approveSignupRequest(Long signupRequestId) {
        SignupForm signupForm = signupRequestRepository.findById(signupRequestId)
                .orElseThrow(SignupRequestNotFoundException::raise);

        signupForm.approve();
    }

    public void rejectSignupRequest(Long signupRequestId) {
        SignupForm signupForm = signupRequestRepository.findById(signupRequestId)
                .orElseThrow(SignupRequestNotFoundException::raise);

        signupForm.reject();
    }
}
