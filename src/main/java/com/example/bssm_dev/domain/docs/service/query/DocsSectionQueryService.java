package com.example.bssm_dev.domain.docs.service.query;

import com.example.bssm_dev.domain.docs.exception.DocsSectionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocsSectionQueryService {
    private final DocsSectionRepository docsSectionRepository;


    public DocsSection findById(Long sectionId) {
        return docsSectionRepository.findById(sectionId)
        .orElseThrow(DocsSectionNotFoundException::raise);
    }
}
