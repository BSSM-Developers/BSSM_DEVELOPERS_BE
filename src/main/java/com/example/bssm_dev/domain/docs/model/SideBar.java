package com.example.bssm_dev.domain.docs.model;

import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sidebar")
public class SideBar {
    @Id
    private String id;
    private String mappedId;
    private DocumentType type;
    private List<SideBarBlock> sideBarBlocks;
}
