package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface DocsRepository extends MongoRepository<Docs, String>, DocsQueryRepository{

    @Query(value = "{ 'title': ?0, 'deletedAt': null }", exists = true)
    boolean existsByTitle(String title);

    @Query(value = "{ 'title': ?0, '_id': { '$ne': ?1 }, 'deletedAt': null }", exists = true)
    boolean existsByTitleAndIdNot(String title, String id);

    @Query("{ '_id': ?0, 'deletedAt': null }")
    Optional<Docs> findByIdAndNotDeleted(String id);

    // 모든 문서 조회 (type 필터 없음)
    Slice<Docs> findAllByOrderByIdDesc(Pageable pageable);
    Slice<Docs> findByIdLessThanOrderByIdDesc(String cursor, Pageable pageable);
    
    // type별 문서 조회
    Slice<Docs> findByTypeOrderByIdDesc(DocumentType type, Pageable pageable);
    Slice<Docs> findByTypeAndIdLessThanOrderByIdDesc(DocumentType type, String cursor, Pageable pageable);
    
    // writerId별 문서 조회 (내가 작성한 문서)
    Slice<Docs> findByWriterIdOrderByIdDesc(Long writerId, Pageable pageable);
    Slice<Docs> findByWriterIdAndIdLessThanOrderByIdDesc(Long writerId, String cursor, Pageable pageable);
    
    // writerId + type별 문서 조회
    Slice<Docs> findByWriterIdAndTypeOrderByIdDesc(Long writerId, DocumentType type, Pageable pageable);
    Slice<Docs> findByWriterIdAndTypeAndIdLessThanOrderByIdDesc(Long writerId, DocumentType type, String cursor, Pageable pageable);
}
