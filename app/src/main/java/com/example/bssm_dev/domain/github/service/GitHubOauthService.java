package com.example.bssm_dev.domain.github.service;

import com.example.bssm_dev.domain.auth.repository.RefreshTokenRepository;
import com.example.bssm_dev.domain.github.dto.response.GitHubTokenResponse;
import com.example.bssm_dev.domain.github.dto.response.GitHubUserResponse;
import com.example.bssm_dev.domain.github.model.GitHubConnection;
import com.example.bssm_dev.domain.github.repository.GitHubConnectionRepository;
import com.example.bssm_dev.domain.user.exception.UserNotFoundException;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import com.example.bssm_dev.global.config.properties.GitHubAppProperties;
import com.example.bssm_dev.global.feign.GitHubTokenFeign;
import com.example.bssm_dev.global.feign.GitHubUserFeign;
import com.example.bssm_dev.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class GitHubOauthService {

    private final GitHubTokenFeign gitHubTokenFeign;
    private final GitHubUserFeign gitHubUserFeign;
    private final GitHubConnectionRepository gitHubConnectionRepository;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GitHubAppProperties gitHubAppProperties;
    private final JwtProvider jwtProvider;

    public record ConnectResult(String githubLogin, String installUrl, String accessToken, String refreshToken) {}

    @Transactional("transactionManager")
    public ConnectResult connect(Long userId, String code) {
        GitHubTokenResponse tokenResponse = exchangeToken(code);
        GitHubUserResponse gitHubUser = fetchGitHubUser(tokenResponse.access_token());

        saveOrUpdateConnection(userId, gitHubUser, tokenResponse);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::raise);
        user.upgradeToApiMaker();
        userRepository.save(user);

        refreshTokenRepository.deleteByUserId(userId);
        String accessToken = jwtProvider.generateAccessToken(userId, user.getEmail(), user.getRole().name());
        String refreshToken = jwtProvider.generateRefreshToken(userId, user.getEmail(), user.getRole().name());

        return new ConnectResult(gitHubUser.login(), buildInstallUrl(), accessToken, refreshToken);
    }

    private GitHubTokenResponse exchangeToken(String code) {
        String body = "client_id=" + enc(gitHubAppProperties.getClientId())
                + "&client_secret=" + enc(gitHubAppProperties.getClientSecret())
                + "&code=" + enc(code)
                + "&redirect_uri=" + enc(gitHubAppProperties.getRedirectUri());

        return gitHubTokenFeign.getToken("application/json", body);
    }

    private GitHubUserResponse fetchGitHubUser(String accessToken) {
        return gitHubUserFeign.getUser("Bearer " + accessToken);
    }

    private void saveOrUpdateConnection(Long userId, GitHubUserResponse gitHubUser,
                                         GitHubTokenResponse tokenResponse) {
        gitHubConnectionRepository.findByUserId(userId)
                .ifPresentOrElse(
                        conn -> {
                            conn.updateTokens(tokenResponse.access_token(), tokenResponse.refresh_token());
                            gitHubConnectionRepository.save(conn);
                        },
                        () -> gitHubConnectionRepository.save(GitHubConnection.of(
                                userId,
                                gitHubUser.id(),
                                gitHubUser.login(),
                                tokenResponse.access_token(),
                                tokenResponse.refresh_token()
                        ))
                );
    }

    private String buildInstallUrl() {
        return "https://github.com/apps/" + gitHubAppProperties.getAppSlug() + "/installations/new";
    }

    private String enc(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
