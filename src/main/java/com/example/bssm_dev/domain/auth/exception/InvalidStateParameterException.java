package com.example.bssm_dev.domain.auth.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class InvalidStateParameterException extends GlobalException {

    private InvalidStateParameterException() {
        super(ErrorCode.INVALID_STATE_PARAMETER);
    }

    static class Holder {
        private static final InvalidStateParameterException INSTANCE = new InvalidStateParameterException();
    }

    public static InvalidStateParameterException raise() {
        return Holder.INSTANCE;
    }
}