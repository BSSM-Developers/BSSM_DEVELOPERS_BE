package com.example.bssm_dev.domain.user.service;

import com.example.bssm_dev.domain.user.dto.request.UserRequest;
import com.example.bssm_dev.domain.user.dto.response.UserLoginResponse;
import com.example.bssm_dev.domain.user.mapper.UserMapper;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional("transactionManager")
    public UserLoginResponse registerIfNotExists(UserRequest userRequest) {
        String email = userRequest.email();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = userMapper.toUser(userRequest);
                    return userRepository.save(newUser);
                });

        UserLoginResponse userLoginResponse = userMapper.toUserLoginResponse(user);
        return userLoginResponse;
    }

}
