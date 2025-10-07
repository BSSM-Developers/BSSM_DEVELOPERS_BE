package com.example.bssm_dev.domain.signup.controller;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.signup.dto.request.UpdatePurposeRequest;
import com.example.bssm_dev.domain.signup.service.SignupRequestService;
import com.example.bssm_dev.domain.user.model.User;
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
            @RequestBody UpdatePurposeRequest request,
            @CurrentUser User user
    ) {
        signupRequestService.updatePurpose(signupRequestId, request);
        ResponseDto<Void> responseDto = HttpUtil.success("successfully update purpose");
        return ResponseEntity.ok(responseDto);
    }
}
