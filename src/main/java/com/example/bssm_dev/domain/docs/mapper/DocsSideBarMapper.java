package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.dto.request.SideBarBlockRequest;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DocsSideBarMapper {
    private final DocsSideBarBlockMapper docsSideBarBlockMapper;

    public SideBar toDocsSideBar(CreateDocsSideBarRequest request, Docs newDocs) {
        SideBar sideBar = SideBar.builder()
                .docsId(newDocs.getId())
                .sideBarBlocks(
                        docsSideBarBlockMapper.toDocsSideBarBlocks(request.blocks())
                )
                .build();
        return sideBar;
    }
}
