package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class InvalidDocsTypeValueException extends GlobalException {
    public InvalidDocsTypeValueException() {
        super(ErrorCode.INVALID_DOCS_TYPE_VALUE);
    }

    static class Holder {
        private static final  InvalidDocsTypeValueException INSTANCE = new InvalidDocsTypeValueException();
    }

    public static InvalidDocsTypeValueException raise() {
        return  Holder.INSTANCE;
    }
}
