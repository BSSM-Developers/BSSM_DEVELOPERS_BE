package com.example.bssm_dev.domain.docs.model;

import lombok.Builder;

import java.util.List;

@Builder
public class SideBarBlock {
    private String id;
    private String mappedId;
    private String label;
    private String module;
    private List<SideBarBlock> childrenItems;
}

