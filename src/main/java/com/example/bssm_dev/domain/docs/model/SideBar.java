package com.example.bssm_dev.domain.docs.model;

import com.example.bssm_dev.domain.docs.model.type.DocsType;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sidebar")
public class SideBar {
    @Id
    private String id;
    private DocsType type;
    private List<SideBarBlock> sideBarBlocks;
}
