package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.ApiDocumentData;
import com.example.bssm_dev.domain.docs.mapper.ApiDocumentMapper;
import com.example.bssm_dev.domain.docs.model.ApiDocument;
import com.example.bssm_dev.domain.docs.repository.ApiDocumentRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiDocumentCommandService {
    private final ApiDocumentRepository apiDocumentRepository;
    private final ApiDocumentMapper apiDocumentMapper;

    public void saveApiDocuments(List<ApiDocumentData> apiDocuments) {
        List<ApiDocument> documents = apiDocumentMapper.toApiDocumentList(apiDocuments);
        apiDocumentRepository.saveAll(documents);
    }

    public void deleteByApiId(Long apiId) {
        apiDocumentRepository.deleteByApiId(apiId);
    }
}
