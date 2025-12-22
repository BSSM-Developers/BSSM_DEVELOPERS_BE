package com.example.bssm_dev.domain.user.service;

import com.example.bssm_dev.domain.user.dto.response.UserLoginResponse;
import com.example.bssm_dev.domain.user.exception.UserNotFoundException;
import com.example.bssm_dev.domain.user.mapper.UserMapper;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public boolean isUserExists(String email) {
        return userRepository.existsByEmail(email);
    }



    public UserLoginResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::raise);

        return userMapper.toUserLoginResponse(user);
    }

    public User findById(Long userId) {
        return  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::raise);
    }
}
