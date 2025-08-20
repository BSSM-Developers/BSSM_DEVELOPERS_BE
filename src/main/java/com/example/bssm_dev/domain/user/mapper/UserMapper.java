package com.example.bssm_dev.domain.user.mapper;

import com.example.bssm_dev.domain.user.dto.request.UserRequest;
import com.example.bssm_dev.domain.user.dto.response.UserLoginResponse;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.model.type.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(UserRequest userRequest) {
        String email = userRequest.email();
        String name = userRequest.name();
        String profile = userRequest.profile();
        UserRole role = UserRole.USER;

        return User.of(email, name, profile, role);
    }

    public UserLoginResponse toUserLoginResponse(User user) {
        return new  UserLoginResponse(user.getUserId(), user.getEmail(), user.getRole().toString());
    }
}
