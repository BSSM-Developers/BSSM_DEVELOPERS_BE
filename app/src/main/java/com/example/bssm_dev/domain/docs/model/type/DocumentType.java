package com.example.bssm_dev.domain.docs.model.type;

import com.example.bssm_dev.domain.docs.exception.InvalidDocsTypeValueException;

public enum DocumentType {
    CUSTOMIZE,
    ORIGINAL;

    public static DocumentType fromString(String type) {
        try {
            return type == null ? null : DocumentType.valueOf(type.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw InvalidDocsTypeValueException.raise();
        }
    }
}
