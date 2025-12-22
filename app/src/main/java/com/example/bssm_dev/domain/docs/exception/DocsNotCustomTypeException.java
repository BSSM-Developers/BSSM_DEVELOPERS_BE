package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class DocsNotCustomTypeException extends GlobalException {

    private DocsNotCustomTypeException() {
        super(ErrorCode.DOCS_NOT_CUSTOM_TYPE);
    }

    static class Holder {
        private static final DocsNotCustomTypeException INSTANCE = new DocsNotCustomTypeException();
    }

    public static DocsNotCustomTypeException raise() {
        return Holder.INSTANCE;
    }
}