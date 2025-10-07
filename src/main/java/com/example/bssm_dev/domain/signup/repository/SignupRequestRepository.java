package com.example.bssm_dev.domain.signup.repository;

import com.example.bssm_dev.domain.signup.model.SignupForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignupRequestRepository extends JpaRepository<SignupForm, Long> {
    Optional<SignupForm> findByEmail(String email);
    boolean existsByEmail(String email);
}