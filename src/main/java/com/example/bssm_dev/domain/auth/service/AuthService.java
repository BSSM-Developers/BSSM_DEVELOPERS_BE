package com.example.bssm_dev.domain.auth.service;

import com.example.bssm_dev.domain.auth.dto.response.TokenResponse;
import com.example.bssm_dev.domain.auth.exception.ExpiredTokenException;
import com.example.bssm_dev.domain.auth.exception.InvalidTokenException;
import com.example.bssm_dev.domain.auth.exception.RefreshTokenNotFoundException;
import com.example.bssm_dev.domain.auth.model.RefreshToken;
import com.example.bssm_dev.domain.auth.repository.RefreshTokenRepository;
import com.example.bssm_dev.global.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final static String REFRESH_TOKEN = "REFRESH_TOKEN";

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        try {
            String tokenType = jwtProvider.getTokenType(refreshToken);
            boolean equalsRefreshToken = REFRESH_TOKEN.equals(tokenType);

            if (!equalsRefreshToken) {
                throw InvalidTokenException.raise();
            }

            RefreshToken storedToken = refreshTokenRepository.findById(refreshToken)
                    .orElseThrow(RefreshTokenNotFoundException::raise);

            Long userId = storedToken.getUserId();
            String email = storedToken.getEmail();
            String role = storedToken.getUserRole();

            String newAccessToken = jwtProvider.generateAccessToken(userId, email, role);
            String newRefreshToken = jwtProvider.generateRefreshToken(userId, email, role);

            refreshTokenRepository.delete(storedToken);

            return new TokenResponse(newAccessToken, newRefreshToken);

        } catch (ExpiredJwtException e) {
            throw ExpiredTokenException.raise();
        } catch (JwtException e) {
            throw InvalidTokenException.raise();
        }
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findById(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }
}
