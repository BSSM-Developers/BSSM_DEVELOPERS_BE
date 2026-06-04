package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class DocsReferencePageAlreadyExistsException extends GlobalException {

    private DocsReferencePageAlreadyExistsException() {
        super(ErrorCode.DOCS_REFERENCE_PAGE_ALREADY_EXISTS);
    }

    static class Holder {
        private static final DocsReferencePageAlreadyExistsException INSTANCE = new DocsReferencePageAlreadyExistsException();
    }

    public static DocsReferencePageAlreadyExistsException raise() {
        return Holder.INSTANCE;
    }
}
