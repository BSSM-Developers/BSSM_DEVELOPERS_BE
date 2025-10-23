package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class DocsPageNotApiPageException extends GlobalException {

    private DocsPageNotApiPageException() {
        super(ErrorCode.DOCS_PAGE_NOT_API_PAGE);
    }

    static class Holder {
        private static final DocsPageNotApiPageException INSTANCE = new DocsPageNotApiPageException();
    }

    public static DocsPageNotApiPageException raise() {
        return Holder.INSTANCE;
    }
}
