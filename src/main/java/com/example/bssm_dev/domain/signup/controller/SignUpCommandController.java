package com.example.bssm_dev.domain.signup.controller;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.signup.dto.request.UpdatePurposeRequest;
import com.example.bssm_dev.domain.signup.service.SignupCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpCommandController {
    private final SignupCommandService signupCommandService;

    @PatchMapping("/{signupRequestId}/purpose")
    public ResponseEntity<ResponseDto<Void>> updatePurpose(
            @PathVariable Long signupRequestId,
            @RequestBody UpdatePurposeRequest request,
            @CookieValue("signup_token") String signupToken
    ) {
        signupCommandService.updatePurpose(signupRequestId, request, signupToken);
        ResponseDto<Void> responseDto = HttpUtil.success("successfully update purpose");
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{signupRequestId}/approve")
    public ResponseEntity<ResponseDto<Void>> approveSignupRequest(
            @PathVariable Long signupRequestId
    ) {
        signupCommandService.approveSignupRequest(signupRequestId);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully approved signup request");
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{signupRequestId}/reject")
    public ResponseEntity<ResponseDto<Void>> rejectSignupRequest(
            @PathVariable Long signupRequestId
    ) {
        signupCommandService.rejectSignupRequest(signupRequestId);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully rejected signup request");
        return ResponseEntity.ok(responseDto);
    }
}
