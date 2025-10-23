package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class DocsPageMismatchException extends GlobalException {

    private DocsPageMismatchException() {
        super(ErrorCode.DOCS_PAGE_MISMATCH);
    }

    static class Holder {
        private static final DocsPageMismatchException INSTANCE = new DocsPageMismatchException();
    }

    public static DocsPageMismatchException raise() {
        return Holder.INSTANCE;
    }
}
