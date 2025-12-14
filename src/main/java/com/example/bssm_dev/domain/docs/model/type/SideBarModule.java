package com.example.bssm_dev.domain.docs.model.type;

import jakarta.validation.constraints.NotNull;
import com.example.bssm_dev.domain.docs.exception.InvalidSideBarModuleException;

public enum SideBarModule {
    DEFAULT, SMALL, COLLAPSE, MAIN, MAIN_TITLE, API;

    public static SideBarModule fromString(String module) {
        try {
            return SideBarModule.valueOf(module.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw InvalidSideBarModuleException.raise();
        }
    }

    public static boolean isApi(String module) {
        return API.equals(fromString(module));
    }
}
