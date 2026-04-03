package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsPageRequest;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.exception.DocsPageNotFoundException;
import com.example.bssm_dev.domain.docs.exception.DocsReferencePageNotEditableException;
import com.example.bssm_dev.domain.docs.mapper.DocsPageBlockMapper;
import com.example.bssm_dev.domain.docs.mapper.DocsPageMapper;
import com.example.bssm_dev.domain.docs.model.ContentDocsPage;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsPageBlock;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
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

    public List<DocsPage> save(List<CreateDocsPageRequest> requests, Docs docs) {
        if (docs.getType() == DocumentType.CUSTOMIZE) {
            DocsValidator.checkCustomDocsPages(requests);
        }
        List<DocsPage> docsPages = docsPageMapper.toDocsPages(requests, docs);
        return docsPageRepository.saveAll(docsPages);
    }

    public void save(DocsPage docsPage) {
        docsPageRepository.save(docsPage);
    }

    public List<String> findAllIdsByDocsId(String docsId) {
        return docsPageRepository.findAllByDocsId(docsId)
                .stream()
                .map(DocsPage::getId)
                .toList();
    }

    public void delete(String docsId) {
        docsPageRepository.deleteByDocsId(docsId);
    }

    public void update(String docsId, String mappedId, UpdateDocsPageRequest request, User user) {
        Docs docs = docsRepository.findByIdAndNotDeleted(docsId)
                .orElseThrow(DocsNotFoundException::raise);
        DocsValidator.checkIfIsMyDocs(user, docs);

        DocsPage docsPage = docsPageRepository.findByDocsIdAndMappedId(docsId, mappedId)
                .orElseThrow(DocsPageNotFoundException::raise);

        if (!(docsPage instanceof ContentDocsPage contentPage)) {
            throw DocsReferencePageNotEditableException.raise();
        }

        List<DocsPageBlock> updatedBlocks = docsPageBlockMapper.toDocsPageBlocks(request.docsBlocks());
        contentPage.updateDocsBlocks(updatedBlocks);

        docsPageRepository.save(contentPage);
    }
}
