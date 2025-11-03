package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.exception.DocsSideBarNotFoundException;
import com.example.bssm_dev.domain.docs.mapper.DocsSideBarBlockMapper;
import com.example.bssm_dev.domain.docs.mapper.DocsSideBarMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.docs.repository.DocsSideBarRepository;
import com.example.bssm_dev.domain.docs.validator.DocsValidator;
import com.example.bssm_dev.domain.user.model.User;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocsSideBarCommandService {
    private final DocsSideBarRepository docsSideBarRepository;
    private final DocsSideBarMapper docsSideBarMapper;
    private final DocsSideBarBlockMapper docsSideBarBlockMapper;
    private final DocsRepository docsRepository;

    public void save(CreateDocsSideBarRequest request, Docs newDocs) {
        SideBar sideBar = docsSideBarMapper.toDocsSideBar(request, newDocs);
        docsSideBarRepository.save(sideBar);
    }

    public void save(SideBar sideBar) {
        docsSideBarRepository.save(sideBar);
    }

    public void delete(String docsId) {
        docsSideBarRepository.deleteByDocsId(docsId);
    }

    public void update(String docsId, UpdateDocsSideBarRequest request, User user) {
        Docs docs = docsRepository.findById(docsId)
                .orElseThrow(DocsNotFoundException::raise);
        DocsValidator.checkIfIsMyDocs(user, docs);

        SideBar sideBar = docsSideBarRepository.findByDocsId(docsId)
                .orElseThrow(DocsSideBarNotFoundException::raise);

        List<SideBarBlock> updatedBlocks = docsSideBarBlockMapper.toDocsSideBarBlocks(request.blocks());
        sideBar.updateSideBarBlocks(updatedBlocks);

        docsSideBarRepository.save(sideBar);
    }
}
