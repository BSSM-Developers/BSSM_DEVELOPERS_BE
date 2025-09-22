package com.example.bssm_dev.domain.signup.repository;

import com.example.bssm_dev.domain.signup.model.SignupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignupRequestRepository extends JpaRepository<SignupRequest, Long> {
    Optional<SignupRequest> findByEmail(String email);
    boolean existsByEmail(String email);
}