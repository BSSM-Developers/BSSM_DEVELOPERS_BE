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
    private final UserRepository userRepository;

    @Transactional
    public DocsResponse createDocs(CreateDocsRequest request, User creator) {
        log.info(creator.toString());
        log.info("user id : {}", creator.getUserId());
        log.info("user name : {}", creator.getName());
        log.info("user email : {}", creator.getEmail());
        // Fetch user from DB to ensure it's in persistent state
        User persistentCreator = userRepository.findById(creator.getUserId())
                .orElseThrow(UserNotFoundException::raise);


        Docs docs = docsMapper.toEntity(request, persistentCreator);
        Docs savedDocs = docsRepository.save(docs);

        return docsMapper.toResponse(savedDocs);
    }
}
