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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "GitHub OAuth", description = "GitHub 계정 연동 및 연결 상태 API")
@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GitHubOauthController {

    private final GitHubOauthService gitHubOauthService;
    private final GitHubConnectionQueryService gitHubConnectionQueryService;
    private final GitHubAppProperties gitHubAppProperties;
    private final CookieUtil cookieUtil;

    @Operation(
            summary = "GitHub OAuth 인증 URL 조회",
            description = "GitHub OAuth 로그인을 시작하기 위한 인증 URL을 반환합니다. " +
                          "프론트엔드는 이 URL로 사용자를 리다이렉트하여 GitHub 로그인을 진행합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 URL 반환 성공")
    })
    @GetMapping("/authorize-url")
    public ResponseEntity<ResponseDto<Map<String, String>>> getAuthorizeUrl() {
        String url = "https://github.com/login/oauth/authorize"
                + "?client_id=" + gitHubAppProperties.getClientId()
                + "&redirect_uri=" + gitHubAppProperties.getRedirectUri();

        return ResponseEntity.ok(HttpUtil.success("authorize url", Map.of("url", url)));
    }

    @Operation(
            summary = "GitHub OAuth 연동",
            description = "GitHub OAuth 콜백으로 전달받은 code를 사용해 GitHub 계정을 연동합니다. " +
                          "연동 성공 시 사용자 Role이 API_MAKER로 승격되며, JWT가 재발급됩니다. " +
                          "응답의 installUrl로 이동해 GitHub App을 설치해야 레포지토리 등록이 가능합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "GitHub 연동 성공 — Set-Cookie로 refresh_token 발급"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
    })
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

    @Operation(
            summary = "GitHub 연결 상태 조회",
            description = "현재 로그인한 사용자의 GitHub 연동 상태를 조회합니다. " +
                          "appInstalled가 false이면 GitHub App 설치가 필요합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "연결 상태 조회 성공"),
            @ApiResponse(responseCode = "404", description = "GitHub 연동 내역 없음")
    })
    @GetMapping("/connection")
    public ResponseEntity<ResponseDto<GitHubConnectionResponse>> getConnection(@CurrentUser User user) {
        GitHubConnectionResponse response = gitHubConnectionQueryService.getConnection(user.getUserId());
        return ResponseEntity.ok(HttpUtil.success("github connection", response));
    }
}
