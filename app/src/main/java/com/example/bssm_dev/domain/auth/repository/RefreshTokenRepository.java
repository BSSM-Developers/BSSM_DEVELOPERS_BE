package com.example.bssm_dev.domain.auth.repository;

import com.example.bssm_dev.domain.auth.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {
}
