package com.example.bssm_dev.domain.auth.repository;

import com.example.bssm_dev.domain.auth.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    List<RefreshToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
