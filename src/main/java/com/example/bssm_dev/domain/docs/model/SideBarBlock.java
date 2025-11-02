package com.example.bssm_dev.domain.docs.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class SideBarBlock {
    private String id;
    private String label;
    private String module;
}
