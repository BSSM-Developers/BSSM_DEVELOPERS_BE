package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class DocsTitleAlreadyExistsException extends GlobalException {

    private DocsTitleAlreadyExistsException() {
        super(ErrorCode.DOCS_TITLE_ALREADY_EXISTS);
    }

    static class Holder {
        private static final DocsTitleAlreadyExistsException INSTANCE = new DocsTitleAlreadyExistsException();
    }

    public static DocsTitleAlreadyExistsException raise() {
        return Holder.INSTANCE;
    }
}
