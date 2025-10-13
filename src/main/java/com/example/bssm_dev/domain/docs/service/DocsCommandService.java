package com.example.bssm_dev.domain.docs.service;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsRequest;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocsCommandService {
    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;

    @Transactional
    public void createDocs(CreateDocsRequest request, User creator) {
        Docs docs = docsMapper.toEntity(request, creator);
        Docs savedDocs = docsRepository.save(docs);
    }
}
