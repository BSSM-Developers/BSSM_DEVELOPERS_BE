package com.example.bssm_dev.domain.docs.model.type;

import com.example.bssm_dev.domain.docs.exception.InvalidDocsTypeValueException;

public enum DocsType {
    CUSTOMIZE,
    ORIGINAL;

    public static DocsType fromString(String type) {
        try {
            return type == null ? null : DocsType.valueOf(type.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw InvalidDocsTypeValueException.raise();
        }
    }
}
