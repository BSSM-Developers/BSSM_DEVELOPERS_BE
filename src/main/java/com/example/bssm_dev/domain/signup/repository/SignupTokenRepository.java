package com.example.bssm_dev.domain.signup.repository;

import com.example.bssm_dev.domain.signup.model.SignupToken;
import org.springframework.data.repository.CrudRepository;

public interface SignupTokenRepository extends CrudRepository<SignupToken, String> {
}
