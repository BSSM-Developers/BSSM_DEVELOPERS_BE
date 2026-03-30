package com.example.bssm_dev.domain.user.controller.query;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.user.dto.response.UserResponse;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * ID 목록으로 사용자 정보 조회
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<UserResponse>>> getUsersByIds(
            @RequestParam List<Long> ids
    ) {
        List<UserResponse> response = userQueryService.getUsersByIds(ids);
        ResponseDto<List<UserResponse>> responseDto = HttpUtil.success("Successfully retrieved users", response);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * ID로 사용자 정보 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserResponse>> getUserById(
            @PathVariable Long userId
    ) {
        UserResponse response = userQueryService.getUserById(userId);
        ResponseDto<UserResponse> responseDto = HttpUtil.success("Successfully retrieved user", response);
        return ResponseEntity.ok(responseDto);
    }
}
