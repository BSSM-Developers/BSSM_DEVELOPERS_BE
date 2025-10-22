package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class ApiDocumentNotFoundException extends GlobalException {

    private ApiDocumentNotFoundException() {
        super(ErrorCode.DOCS_API_DOCUMENT_NOT_FOUND);
    }

    static class Holder {
        private static final ApiDocumentNotFoundException INSTANCE = new ApiDocumentNotFoundException();
    }

    public static ApiDocumentNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
