package com.example.bssm_dev.global.jwt;

import com.example.bssm_dev.domain.auth.model.RefreshToken;
import com.example.bssm_dev.domain.auth.repository.RefreshTokenRepository;
import com.example.bssm_dev.global.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private static final String ACCESS_TOEKN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateAccessToken(Long userId, String email, String role) {
        return generateToken(userId, email, role, jwtProperties.getAccessExp(), ACCESS_TOEKN);
    }

    public String generateRefreshToken(Long userId, String email, String role) {
        String token = generateToken(userId, email, role, jwtProperties.getRefreshExp(), REFRESH_TOKEN);
        RefreshToken refreshToken = new RefreshToken(token, userId, email, role);
        refreshTokenRepository.save(refreshToken);
        return token;
    }


    public String generateToken(Long userId, String email, String role, Long exp, String type) {
        String strUserId = String.valueOf(userId);
        Date now = new Date();

        return Jwts.builder()
                .setSubject(strUserId)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + exp))
                .claim("typ", type)
                .claim("email", email)
                .claim("role", role)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getTokenType(String token) {
        return getClaims(token).get("typ", String.class);
    }
}
