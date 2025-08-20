package com.example.bssm_dev.domain.user.service;

import com.example.bssm_dev.domain.user.dto.request.UserRequest;
import com.example.bssm_dev.domain.user.mapper.UserMapper;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void registerIfNotExists(UserRequest userRequest) {
        String email = userRequest.email();

        boolean existsUser = userRepository.existsByEmail(email);
        if (!existsUser) {
            User user = userMapper.toUser(userRequest);
            userRepository.save(user);
        }
    }
}
