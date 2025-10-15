package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class DocsNotFoundException extends GlobalException {

    private DocsNotFoundException() {
        super(ErrorCode.DOCS_NOT_FOUND);
    }

    static class Holder {
        private static final DocsNotFoundException INSTANCE = new DocsNotFoundException();
    }

    public static DocsNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
