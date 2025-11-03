package com.example.bssm_dev.domain.docs.model.type;

public enum SideBarModule {
    DEFAULT, SMALL, COLLAPSE, MAIN, MAINTITLE, API;

    public static SideBarModule fromString(String module) {
        return SideBarModule.valueOf(module.toUpperCase());
    }
}
