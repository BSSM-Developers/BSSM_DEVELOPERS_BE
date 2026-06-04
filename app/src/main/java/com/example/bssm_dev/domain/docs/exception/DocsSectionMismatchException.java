package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

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
