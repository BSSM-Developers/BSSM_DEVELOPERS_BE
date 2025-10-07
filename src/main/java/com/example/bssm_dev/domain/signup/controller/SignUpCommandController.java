package com.example.bssm_dev.domain.signup.controller;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.signup.dto.request.UpdatePurposeRequest;
import com.example.bssm_dev.domain.signup.service.SignupRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpCommandController {

    private final SignupRequestService signupRequestService;

    @PatchMapping("/{signupRequestId}/purpose")
    public ResponseEntity<ResponseDto<Void>> updatePurpose(
            @PathVariable Long signupRequestId,
            @RequestBody UpdatePurposeRequest request
    ) {
        signupRequestService.updatePurpose(signupRequestId, request);
        ResponseDto<Void> responseDto = HttpUtil.success("목적이 수정되었습니다.");
        return ResponseEntity.ok(responseDto);
    }
}
