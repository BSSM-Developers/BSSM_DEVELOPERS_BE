package com.example.bssm_dev.domain.user.controller.query;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.user.dto.response.UserResponse;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserQueryController {
    private final UserQueryService userQueryService;

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping
    public ResponseEntity<ResponseDto<UserResponse>> getCurrentUser(
            @CurrentUser User user
    ) {
        UserResponse response = userQueryService.getCurrentUser(user);
        ResponseDto<UserResponse> responseDto = HttpUtil.success("Successfully retrieved current user", response);
        return ResponseEntity.ok(responseDto);
    }
}
