package com.example.bssm_dev.domain.user.mapper;

import com.example.bssm_dev.domain.user.dto.request.UserRequest;
import com.example.bssm_dev.domain.user.dto.response.UserLoginResponse;
import com.example.bssm_dev.domain.user.dto.response.UserResponse;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.model.type.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserRequest userRequest) {
        String email = userRequest.email();
        String name = userRequest.name();
        String profile = userRequest.profile();
        UserRole role = UserRole.ROLE_USER;

        return User.of(email, name, profile, role);
    }

    public UserLoginResponse toUserLoginResponse(User user) {
        return UserLoginResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .profile(user.getProfile())
                .role(
                        String.valueOf(
                                user.getRole()
                        )
                                .substring(5)
                )
                .build();
    }
}
