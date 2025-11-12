package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocsRepository extends MongoRepository<Docs, String>, DocsQueryRepository{
    
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
