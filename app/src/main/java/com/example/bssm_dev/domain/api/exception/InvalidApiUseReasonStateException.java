package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.GlobalException;
import com.example.bssm_dev.exception.ErrorCode;

public class InvalidApiUseReasonStateException extends GlobalException {
    private InvalidApiUseReasonStateException() {
        super(ErrorCode.INVALID_API_USE_REASON_STATE);
    }

    private static class Holder {
        private static final InvalidApiUseReasonStateException INSTANCE = new InvalidApiUseReasonStateException();
    }

    public static InvalidApiUseReasonStateException raise() {
        return Holder.INSTANCE;
    }
}
