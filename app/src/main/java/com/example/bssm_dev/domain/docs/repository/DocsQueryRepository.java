package com.example.bssm_dev.domain.docs.repository;

import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.docs.model.type.ServerStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface DocsQueryRepository {
    Slice<Docs> fetchDocs(DocumentType docsType, ServerStatus serverStatus, String cursor, Pageable pageable);

    Slice<Docs> fetchMyDocs(Long writerId, DocumentType docsType, ServerStatus serverStatus, String cursor, Pageable pageable);

    Slice<Docs> fetchPopularDocs(DocumentType docsType, ServerStatus serverStatus, Long tokenCount, String cursor, Pageable pageable);

    Slice<Docs> fetchMyPopularDocs(Long writerId, DocumentType docsType, ServerStatus serverStatus, Long tokenCount, String cursor, Pageable pageable);
}
