package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class DocsSectionMismatchException extends GlobalException {

    private DocsSectionMismatchException() {
        super(ErrorCode.DOCS_SECTION_MISMATCH);
    }

    static class Holder {
        private static final DocsSectionMismatchException INSTANCE = new DocsSectionMismatchException();
    }

    public static DocsSectionMismatchException raise() {
        return Holder.INSTANCE;
    }
}
