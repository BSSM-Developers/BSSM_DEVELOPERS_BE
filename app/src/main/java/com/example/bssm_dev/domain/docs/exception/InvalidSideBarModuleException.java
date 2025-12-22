package com.example.bssm_dev.domain.docs.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class InvalidSideBarModuleException extends GlobalException {
    private InvalidSideBarModuleException() {
        super(ErrorCode.INVALID_SIDEBAR_MODULE);
    }

    private static class Holder {
        private static final InvalidSideBarModuleException INSTANCE = new InvalidSideBarModuleException();
    }

    public static InvalidSideBarModuleException raise() {
        return Holder.INSTANCE;
    }
}
