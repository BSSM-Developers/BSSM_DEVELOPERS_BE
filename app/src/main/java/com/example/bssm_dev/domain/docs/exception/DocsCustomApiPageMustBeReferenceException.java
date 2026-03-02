package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class DocsCustomApiPageMustBeReferenceException extends GlobalException {

    private DocsCustomApiPageMustBeReferenceException() {
        super(ErrorCode.DOCS_CUSTOM_API_PAGE_MUST_BE_REFERENCE);
    }

    static class Holder {
        private static final DocsCustomApiPageMustBeReferenceException INSTANCE = new DocsCustomApiPageMustBeReferenceException();
    }

    public static DocsCustomApiPageMustBeReferenceException raise() {
        return Holder.INSTANCE;
    }
}
