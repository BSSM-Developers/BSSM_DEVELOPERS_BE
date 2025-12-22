package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class DocsSideBarNotFoundException extends GlobalException {

    private DocsSideBarNotFoundException() {
        super(ErrorCode.DOCS_SIDEBAR_NOT_FOUND);
    }

    static class Holder {
        private static final DocsSideBarNotFoundException INSTANCE = new DocsSideBarNotFoundException();
    }

    public static DocsSideBarNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
