package com.example.bssm_dev.domain.docs.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.docs.dto.response.DocsDetailResponse;
import com.example.bssm_dev.domain.docs.dto.response.DocsListResponse;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocsQueryService {
    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;
}
