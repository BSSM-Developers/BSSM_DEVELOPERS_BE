package com.example.bssm_dev.domain.auth.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class InvalidBssmEmailException extends GlobalException {

    private InvalidBssmEmailException() {
        super(ErrorCode.BSSM_EMAIL_INVALID);
    }

    class Holder {
        private final static  InvalidBssmEmailException INSTANCE = new InvalidBssmEmailException();
    }

    public static InvalidBssmEmailException raise() {
        return Holder.INSTANCE;
    }
}
