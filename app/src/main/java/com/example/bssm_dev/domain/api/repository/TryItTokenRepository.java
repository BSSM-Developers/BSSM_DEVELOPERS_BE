package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.TryItToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TryItTokenRepository extends JpaRepository<TryItToken, Long> {
    Optional<TryItToken> findByApi(Api api);
}
