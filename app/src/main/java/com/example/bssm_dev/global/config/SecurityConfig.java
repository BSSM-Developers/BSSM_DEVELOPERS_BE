package com.example.bssm_dev.global.config;

import com.example.bssm_dev.global.jwt.CustomAccessDeniedHandler;
import com.example.bssm_dev.global.jwt.CustomAuthenticationEntryPoint;
import com.example.bssm_dev.global.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("API_MAKER")
                .role("API_MAKER").implies("USER")
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HttpFirewall httpFirewall) {
        return web -> web.httpFirewall(httpFirewall);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "https://bssmdev.com",
                "https://dev.bssm-dev.com",  // 개발 서버
                "http://localhost",      // 기본 포트 로컬 개발용
                "https://localhost",     // 기본 포트 로컬 개발용 (HTTPS)
                "http://localhost:*",     // 프론트 로컬 개발용
                "https://localhost:*"     // 프론트 로컬 개발용 (HTTPS)
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthorityAuthorizationManager<RequestAuthorizationContext> requiresApiMaker =
                AuthorityAuthorizationManager.hasRole("API_MAKER");
        requiresApiMaker.setRoleHierarchy(roleHierarchy());

        AuthorityAuthorizationManager<RequestAuthorizationContext> requiresAdmin =
                AuthorityAuthorizationManager.hasRole("ADMIN");
        requiresAdmin.setRoleHierarchy(roleHierarchy());

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/webhook/github").permitAll()
                        .requestMatchers("/signup/me").permitAll()
                        .requestMatchers("/signup/*/purpose").permitAll()
                        .requestMatchers("/signup/**").access(requiresAdmin)
                        // docs CUD → API_MAKER 이상 (api-use-requests는 일반 인증 사용자 허용)
                        .requestMatchers(HttpMethod.POST, "/docs/original", "/docs/custom").access(requiresApiMaker)
                        .requestMatchers(HttpMethod.POST, "/docs/**/api-use-requests").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/docs/**").access(requiresApiMaker)
                        .requestMatchers(HttpMethod.PUT, "/docs/**").access(requiresApiMaker)
                        .requestMatchers(HttpMethod.PATCH, "/docs/**").access(requiresApiMaker)
                        .requestMatchers("/docs/**").permitAll()
                        .requestMatchers("/apis/*/try-it-token").permitAll()
                        .requestMatchers("/api/proxy/**").permitAll()
                        .requestMatchers("/api/proxy-server/**").permitAll()
                        .requestMatchers("/api/proxy-browser/**").permitAll()
                        .requestMatchers("/api/healthy/**").permitAll()
                        .requestMatchers("/api/token/client/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
