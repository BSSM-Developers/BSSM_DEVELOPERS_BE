package com.example.bssm_dev.domain.docs.model;

import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sidebar")
@Builder
public class SideBar {
    @Id
    private String id;
    private List<SideBarBlock> sideBarBlocks;
    private String docsId;
}
