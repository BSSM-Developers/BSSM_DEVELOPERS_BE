package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsPageRequest;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.exception.DocsPageNotFoundException;
import com.example.bssm_dev.domain.docs.mapper.DocsPageBlockMapper;
import com.example.bssm_dev.domain.docs.mapper.DocsPageMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsPageBlock;
import com.example.bssm_dev.domain.docs.repository.DocsPageRepository;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.docs.validator.DocsValidator;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocsPageCommandService {
    private final DocsPageRepository docsPageRepository;
    private final DocsPageMapper docsPageMapper;
    private final DocsPageBlockMapper docsPageBlockMapper;
    private final DocsRepository docsRepository;

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

    public void update(String docsId, String mappedId, UpdateDocsPageRequest request, User user) {
        Docs docs = docsRepository.findById(docsId)
                .orElseThrow(DocsNotFoundException::raise);
        DocsValidator.checkIfIsMyDocs(user, docs);

        DocsPage docsPage = docsPageRepository.findByDocsIdAndMappedId(docsId, mappedId)
                .orElseThrow(DocsPageNotFoundException::raise);

        List<DocsPageBlock> updatedBlocks = docsPageBlockMapper.toDocsPageBlocks(request.docsBlocks());
        docsPage.updateDocsBlocks(updatedBlocks);

        docsPageRepository.save(docsPage);
    }
}
