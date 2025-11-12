package com.example.bssm_dev.domain.docs.service.query;

import com.example.bssm_dev.domain.docs.dto.response.DocsSideBarResponse;
import com.example.bssm_dev.domain.docs.exception.DocsSideBarNotFoundException;
import com.example.bssm_dev.domain.docs.mapper.DocsSideBarMapper;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.repository.DocsSideBarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocsSideBarQueryService {
    private final DocsSideBarRepository docsSideBarRepository;
    private final DocsSideBarMapper docsSideBarMapper;

    public DocsSideBarResponse getSideBar(String docsId) {
        SideBar sideBar = docsSideBarRepository.findByDocsId(docsId)
                .orElseThrow(DocsSideBarNotFoundException::raise);
        
        return docsSideBarMapper.toResponse(sideBar);
    }
}
