package com.example.bssm_dev.global.jwt;

import com.example.bssm_dev.domain.auth.exception.ExpiredTokenException;
import com.example.bssm_dev.domain.auth.exception.InvalidTokenException;
import com.example.bssm_dev.global.config.properties.JwtProperties;
import com.example.bssm_dev.global.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        boolean shouldSkip = path.startsWith("/api/proxy") || path.startsWith("/api/proxy-browser") || path.startsWith("/api/proxy-server");
        log.debug("shouldNotFilter - path: {}, shouldSkip: {}", path, shouldSkip);
        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null) {
            try {
                Claims claims = jwtProvider.getClaims(token);
                String userId = claims.getSubject();
                String email = claims.get("email", String.class);
                String role = claims.get("role", String.class);
                log.debug("Role from token: {}", role);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                log.debug("Token expired: {}", e.getMessage());
                handleException(response, 401, "만료된 토큰입니다.");
            } catch (MalformedJwtException e) {
                log.debug("Malformed token: {}", e.getMessage());
                handleException(response, 401, "토큰 형식이 올바르지 않습니다.");
            } catch (UnsupportedJwtException e) {
                log.debug("Unsupported token: {}", e.getMessage());
                handleException(response, 401, "지원되지 않는 토큰 형식입니다.");
            } catch (JwtException e) {
                log.debug("Invalid token: {} - {}", e.getClass().getSimpleName(), e.getMessage());
                handleException(response, 401, "유효하지 않은 토큰입니다.");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeader());
        log.debug("Header name: {}, Header value: {}, Expected prefix: '{}'",
                jwtProperties.getHeader(), bearerToken, jwtProperties.getPrefix());

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getPrefix())) {
            return bearerToken.substring(jwtProperties.getPrefix().length()).trim();
        }

        return null;
    }


    private void handleException(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(statusCode, message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
