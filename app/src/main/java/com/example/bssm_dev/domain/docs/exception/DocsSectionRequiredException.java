package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class DocsSectionRequiredException extends GlobalException {

    private DocsSectionRequiredException() {
        super(ErrorCode.DOCS_SECTION_REQUIRED);
    }

    static class Holder {
        private static final DocsSectionRequiredException INSTANCE = new DocsSectionRequiredException();
    }

    public static DocsSectionRequiredException raise() {
        return Holder.INSTANCE;
    }
}