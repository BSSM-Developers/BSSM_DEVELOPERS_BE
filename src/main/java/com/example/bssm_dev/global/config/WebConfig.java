package com.example.bssm_dev.global.config;

import com.example.bssm_dev.common.resolver.CurrentUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        log.info("[WebConfig] Registering CurrentUserArgumentResolver");
        resolvers.add(currentUserArgumentResolver);
    }

    @Override
    public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
        log.info("[WebConfig] Configuring CORS mappings");
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "https://bssmdev.com",
                        "https://dev.bssm-dev.com",  // 개발 서버
                        "http://localhost",      // 기본 포트 로컬 개발용
                        "https://localhost",     // 기본 포트 로컬 개발용 (HTTPS)
                        "http://localhost:*",     // 프론트 로컬 개발용
                        "https://localhost:*"     // 프론트 로컬 개발용 (HTTPS)
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
