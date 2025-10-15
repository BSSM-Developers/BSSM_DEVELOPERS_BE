package com.example.bssm_dev.common.resolver;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.domain.user.exception.UserNotFoundException;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(CurrentUser.class);
        boolean isUserType = parameter.getParameterType().equals(User.class);
        log.info("[CurrentUserArgumentResolver] supportsParameter - hasAnnotation: {}, isUserType: {}", hasAnnotation, isUserType);
        return hasAnnotation && isUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw UserNotFoundException.raise();
        }

        String userId = (String) authentication.getPrincipal();
        log.info("[CurrentUserArgumentResolver] userId from JWT: {}", userId);
        
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(UserNotFoundException::raise);
        
        log.info("[CurrentUserArgumentResolver] Found user - id: {}, email: {}, name: {}", 
                user.getUserId(), user.getEmail(), user.getName());
        
        return user;
    }
}
