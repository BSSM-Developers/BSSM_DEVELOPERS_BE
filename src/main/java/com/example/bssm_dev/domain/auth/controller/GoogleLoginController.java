package com.example.bssm_dev.domain.auth.controller;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.domain.auth.service.GoogleLoginService;
import com.example.bssm_dev.global.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/google")
public class GoogleLoginController {
    private final GoogleLoginService googleLoginService;

    @GetMapping
    public ResponseEntity<ResponseDto<String>> showGoogleLoginUrl() {
        String url = googleLoginService.getUrl();
        ResponseDto<String> responseDto = HttpUtil.success(url);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/callback")
    public void googleLoginCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        googleLoginService.registerOrLogin(code, state);
    }
}
