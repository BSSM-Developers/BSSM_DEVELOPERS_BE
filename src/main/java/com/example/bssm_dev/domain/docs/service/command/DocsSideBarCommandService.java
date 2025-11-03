package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.mapper.DocsSideBarMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.repository.DocsSideBarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocsSideBarCommandService {
    private final DocsSideBarRepository docsSideBarRepository;
    private final DocsSideBarMapper docsSideBarMapper;

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
}
