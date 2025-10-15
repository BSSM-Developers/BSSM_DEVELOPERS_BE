package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class UnauthorizedDocsAccessException extends GlobalException {

    private UnauthorizedDocsAccessException() {
        super(ErrorCode.UNAUTHORIZED_DOCS_ACCESS);
    }

    static class Holder {
        private static final UnauthorizedDocsAccessException INSTANCE = new UnauthorizedDocsAccessException();
    }

    public static UnauthorizedDocsAccessException raise() {
        return Holder.INSTANCE;
    }
}
