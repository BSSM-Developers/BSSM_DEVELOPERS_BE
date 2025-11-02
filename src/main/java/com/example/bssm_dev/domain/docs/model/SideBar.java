package com.example.bssm_dev.domain.docs.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "sidebar")
public class SideBar {
    @Id
    private String id;
    @Embedded
    private List<SideBarBlock> sideBarBlocks;
}
