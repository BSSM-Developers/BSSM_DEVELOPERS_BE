package com.example.bssm_dev.domain.auth.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class SignupRequestAlreadyExistsException extends GlobalException {

    private SignupRequestAlreadyExistsException() {
        super(ErrorCode.SIGNUP_REQUEST_ALREADY_EXISTS);
    }

    class Holder {
        private final static SignupRequestAlreadyExistsException INSTANCE = new SignupRequestAlreadyExistsException();
    }

    public static SignupRequestAlreadyExistsException raise() {
        return Holder.INSTANCE;
    }
}