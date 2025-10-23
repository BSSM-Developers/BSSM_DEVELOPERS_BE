package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class DocsPageNotFoundException extends GlobalException {

    private DocsPageNotFoundException() {
        super(ErrorCode.DOCS_PAGE_NOT_FOUND);
    }

    static class Holder {
        private static final DocsPageNotFoundException INSTANCE = new DocsPageNotFoundException();
    }

    public static DocsPageNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
