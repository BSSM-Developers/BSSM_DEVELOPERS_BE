package com.example.bssm_dev.domain.docs.model;

import com.example.bssm_dev.domain.docs.model.type.SideBarModule;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SideBarBlock {
    private String id;
    private String mappedId;
    private String label;
    private SideBarModule module;
    private List<SideBarBlock> childrenItems;

    public String getModuleName() {
        return this.module.name().toLowerCase();
    }
}

