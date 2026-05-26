package com.example.bssm_dev.domain.github.controller;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.CookieUtil;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.github.dto.request.GitHubConnectRequest;
import com.example.bssm_dev.domain.github.dto.response.GitHubConnectResponse;
import com.example.bssm_dev.domain.github.dto.response.GitHubConnectionResponse;
import com.example.bssm_dev.domain.github.service.GitHubConnectionQueryService;
import com.example.bssm_dev.domain.github.service.GitHubOauthService;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.global.config.properties.GitHubAppProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GitHubOauthController {

    private final GitHubOauthService gitHubOauthService;
    private final GitHubConnectionQueryService gitHubConnectionQueryService;
    private final GitHubAppProperties gitHubAppProperties;
    private final CookieUtil cookieUtil;

    @GetMapping("/authorize-url")
    public ResponseEntity<ResponseDto<Map<String, String>>> getAuthorizeUrl() {
        String url = "https://github.com/login/oauth/authorize"
                + "?client_id=" + gitHubAppProperties.getClientId()
                + "&redirect_uri=" + gitHubAppProperties.getRedirectUri();

        return ResponseEntity.ok(HttpUtil.success("authorize url", Map.of("url", url)));
    }

    @PostMapping("/connect")
    public ResponseEntity<ResponseDto<GitHubConnectResponse>> connect(
            @CurrentUser User user,
            @RequestBody GitHubConnectRequest request,
            HttpServletResponse httpServletResponse
    ) {
        GitHubOauthService.ConnectResult result = gitHubOauthService.connect(user.getUserId(), request.code());

        httpServletResponse.addHeader("Set-Cookie", cookieUtil.bake("refresh_token", result.refreshToken()).toString());

        GitHubConnectResponse response = GitHubConnectResponse.of(
                result.githubLogin(),
                result.installUrl(),
                result.accessToken()
        );
        return ResponseEntity.ok(HttpUtil.success("github connected", response));
    }

    @GetMapping("/connection")
    public ResponseEntity<ResponseDto<GitHubConnectionResponse>> getConnection(@CurrentUser User user) {
        GitHubConnectionResponse response = gitHubConnectionQueryService.getConnection(user.getUserId());
        return ResponseEntity.ok(HttpUtil.success("github connection", response));
    }
}
