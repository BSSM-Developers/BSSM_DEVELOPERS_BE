package com.example.bssm_dev.domain.user.event.listener;

import com.example.bssm_dev.domain.signup.event.SignupApprovedEvent;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.model.type.UserRole;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignupEventListener {

    private final UserRepository userRepository;

    @EventListener
    @Transactional("transactionManager")
    public void handleSignupApprovedEvent(SignupApprovedEvent event) {
        log.info("SignupApprovedEvent received for email: {}", event.email());

        if (!userRepository.existsByEmail(event.email())) {
            User newUser = User.of(
                    event.email(),
                    event.name(),
                    event.profile(),
                    UserRole.ROLE_USER
            );
            userRepository.save(newUser);
            log.info("Successfully created new User for email: {}", event.email());
        } else {
            log.info("User already exists for email: {}", event.email());
        }
    }
}
