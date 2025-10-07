package com.example.bssm_dev.domain.signup.controller;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.signup.dto.response.SignupResponse;
import com.example.bssm_dev.domain.signup.service.SignupQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpQueryController {
    private final SignupQueryService signupQueryService;

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<SignupResponse>> getMySignup(
            @CookieValue("signup_token") String signupToken
    ) {
        SignupResponse signupResponse = signupQueryService.getMySignup(signupToken);
        ResponseDto<SignupResponse> responseDto = HttpUtil.success("Get my signup", signupResponse);
        return ResponseEntity.ok(responseDto);
    }
}
