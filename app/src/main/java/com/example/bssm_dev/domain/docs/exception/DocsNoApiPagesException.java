package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class DocsNoApiPagesException extends GlobalException {

    private DocsNoApiPagesException() {
        super(ErrorCode.DOCS_NO_API_PAGES);
    }

    static class Holder {
        private static final DocsNoApiPagesException INSTANCE = new DocsNoApiPagesException();
    }

    public static DocsNoApiPagesException raise() {
        return Holder.INSTANCE;
    }
}
