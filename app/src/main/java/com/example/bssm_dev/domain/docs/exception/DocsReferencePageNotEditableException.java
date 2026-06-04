package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class DocsReferencePageNotEditableException extends GlobalException {

    private DocsReferencePageNotEditableException() {
        super(ErrorCode.DOCS_REFERENCE_PAGE_NOT_EDITABLE);
    }

    static class Holder {
        private static final DocsReferencePageNotEditableException INSTANCE = new DocsReferencePageNotEditableException();
    }

    public static DocsReferencePageNotEditableException raise() {
        return Holder.INSTANCE;
    }
}
