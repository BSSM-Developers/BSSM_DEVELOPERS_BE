package com.example.bssm_dev.domain.docs.service;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsRequest;
import com.example.bssm_dev.domain.docs.dto.response.DocsResponse;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.user.exception.UserNotFoundException;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocsCommandService {
    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;

    @Transactional
    public DocsResponse createDocs(CreateDocsRequest request, User creator) {
        Docs docs = docsMapper.toEntity(request, creator);
        Docs savedDocs = docsRepository.save(docs);

        return docsMapper.toResponse(savedDocs);
    }
}
