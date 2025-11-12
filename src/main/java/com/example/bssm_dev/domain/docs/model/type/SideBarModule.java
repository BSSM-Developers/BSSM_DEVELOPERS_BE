package com.example.bssm_dev.domain.docs.model.type;

import jakarta.validation.constraints.NotNull;

public enum SideBarModule {
    DEFAULT, SMALL, COLLAPSE, MAIN, MAIN_TITLE, API;

    public static SideBarModule fromString(String module) {
        return SideBarModule.valueOf(module.toUpperCase());
    }

    public static boolean isApi(String module) {
        return API.equals(fromString(module));
    }
}
