package com.example.bssm_dev.domain.auth.repository;

import com.example.bssm_dev.domain.auth.model.GoogleAuthCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleAuthCodeRepository extends CrudRepository<GoogleAuthCode, String> {
}
