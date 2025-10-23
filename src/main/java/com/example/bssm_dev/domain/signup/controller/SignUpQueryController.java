package com.example.bssm_dev.domain.signup.controller;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.signup.dto.response.SignupResponse;
import com.example.bssm_dev.domain.signup.service.SignupQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpQueryController {
    private final SignupQueryService signupQueryService;

    /**
     * 나의 회원가입 신청 조회
     */
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<SignupResponse>> getMySignup(
            @CookieValue("signup_token") String signupToken
    ) {
        SignupResponse signupResponse = signupQueryService.getMySignup(signupToken);
        ResponseDto<SignupResponse> responseDto = HttpUtil.success("Get my signup", signupResponse);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 회원가입 신청 조회 by 커서 기반 페이지네이션
     */
    @GetMapping
    public ResponseEntity<ResponseDto<CursorPage<SignupResponse>>> getSignupRequests(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String state
    ) {
        CursorPage<SignupResponse> signupRequests = signupQueryService.getSignupRequests(cursor, size, state);
        ResponseDto<CursorPage<SignupResponse>> responseDto = HttpUtil.success("Get signup requests", signupRequests);
        return ResponseEntity.ok(responseDto);
    }
}
