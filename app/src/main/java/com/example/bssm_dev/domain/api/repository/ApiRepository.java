package com.example.bssm_dev.domain.api.repository;

import com.example.bssm_dev.domain.api.model.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApiRepository extends JpaRepository<Api, String>, QuerydslPredicateExecutor<Api> {

    Optional<Api> findByApiGroupApiGroupIdAndIsCurrentTrue(String apiGroupId);

    List<Api> findByApiGroupApiGroupIdInAndIsCurrentTrue(Collection<String> apiGroupIds);

    Optional<Api> findByApiGroupApiGroupIdAndEndpointAndMethodAndIsCurrentTrue(String apiGroupId, String endpoint, String method);

    @Query("SELECT a FROM Api a WHERE a.githubRepositoryId = :repoId AND a.endpoint = :endpoint AND a.method = :method AND a.isCurrent = true")
    Optional<Api> findCurrentApiByRepoAndEndpointAndMethod(
            @Param("repoId") Long repoId,
            @Param("endpoint") String endpoint,
            @Param("method") String method
    );

    List<Api> findAllByGithubRepositoryIdAndIsCurrentTrue(Long githubRepositoryId);

    boolean existsByGithubRepositoryIdAndEndpointAndMethodAndIsCurrentTrue(
            Long githubRepositoryId, String endpoint, String method);
}
