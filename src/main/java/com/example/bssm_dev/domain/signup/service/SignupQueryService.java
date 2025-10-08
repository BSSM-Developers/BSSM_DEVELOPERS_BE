package com.example.bssm_dev.domain.signup.service;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.signup.dto.response.SignupResponse;
import com.example.bssm_dev.domain.signup.exception.InvalidSignupTokenException;
import com.example.bssm_dev.domain.signup.exception.SignupRequestNotFoundException;
import com.example.bssm_dev.domain.signup.mapper.SignupRequestMapper;
import com.example.bssm_dev.domain.signup.model.SignupForm;
import com.example.bssm_dev.domain.signup.model.SignupToken;
import com.example.bssm_dev.domain.signup.model.type.SignUpFormState;
import com.example.bssm_dev.domain.signup.repository.SignupRequestRepository;
import com.example.bssm_dev.domain.signup.repository.SignupTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignupQueryService {

    private final SignupRequestRepository signupRequestRepository;
    private final SignupTokenRepository signupTokenRepository;
    private final SignupRequestMapper signupRequestMapper;


    public SignupResponse getMySignup(String signupToken) {
        SignupToken token = signupTokenRepository.findById(signupToken)
                .orElseThrow(InvalidSignupTokenException::raise);

        SignupForm signupForm = signupRequestRepository.findByEmail(token.getEmail())
                .orElseThrow(SignupRequestNotFoundException::raise);

        return signupRequestMapper.toSignupResponse(signupForm);
    }

    public CursorPage<SignupResponse> getSignupRequests(Long cursor, int size, String stateParam) {
        Pageable pageable = PageRequest.of(0, size);
        SignUpFormState state = SignUpFormState.fromString(stateParam);

        Slice<SignupForm> slice = fetchSignupFormSlice(state, cursor, pageable);
        List<SignupResponse> content = signupRequestMapper.mapToResponseList(slice);

        return new CursorPage<>(content, slice.hasNext());
    }

    private Slice<SignupForm> fetchSignupFormSlice(SignUpFormState state, Long cursor, Pageable pageable) {
        return switch (state) {
            case null -> fetchAllSignupForms(cursor, pageable);
            case PENDING, APPROVED, REJECTED -> fetchSignupFormsByState(state, cursor, pageable);
        };
    }

    // 회원가입 신청 상태 관계없이 전체 조회
    private Slice<SignupForm> fetchAllSignupForms(Long cursor, Pageable pageable) {
       return cursor == null
               ? signupRequestRepository.findAllByOrderBySignupFormIdDesc(pageable)
               : signupRequestRepository.findBySignupFormIdLessThanOrderBySignupFormIdDesc(cursor, pageable);
    }

    // 회원가입 신청 상태에 따라 조회
    private Slice<SignupForm> fetchSignupFormsByState(SignUpFormState state, Long cursor, Pageable pageable) {
        return cursor == null
            ? signupRequestRepository.findByStateOrderBySignupFormIdDesc(state, pageable)
            : signupRequestRepository.findByStateAndSignupFormIdLessThanOrderBySignupFormIdDesc(state, cursor, pageable);
    }

}
