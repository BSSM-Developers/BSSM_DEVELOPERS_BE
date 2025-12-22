package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class DocsSectionNotFoundException extends GlobalException {

    private DocsSectionNotFoundException() {
        super(ErrorCode.DOCS_SECTION_NOT_FOUND);
    }

    static class Holder {
        private static final DocsSectionNotFoundException INSTANCE = new DocsSectionNotFoundException();
    }

    public static DocsSectionNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
