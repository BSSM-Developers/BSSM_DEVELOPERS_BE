package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.mapper.DocsPageMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.repository.DocsPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocsPageCommandService {
    private final DocsPageRepository docsPageRepository;
    private final DocsPageMapper docsPageMapper;

    public void save(List<CreateDocsPageRequest> requests, Docs newDocs) {
        List<DocsPage> docsPages = docsPageMapper.toDocsPages(requests, newDocs);
        docsPageRepository.saveAll(docsPages);
    }

    public void save(DocsPage docsPage) {
        docsPageRepository.save(docsPage);
    }

    public void delete(String docsId) {
        docsPageRepository.deleteByDocsId(docsId);
    }
}
