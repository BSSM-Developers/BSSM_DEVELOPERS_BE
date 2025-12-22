package com.example.bssm_dev.domain.signup.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class SignupRequestNotFoundException extends GlobalException {

    private SignupRequestNotFoundException() {
        super(ErrorCode.SIGNUP_REQUEST_NOT_FOUND);
    }

    static class Holder {
        private static final SignupRequestNotFoundException INSTANCE = new SignupRequestNotFoundException();
    }

    public static SignupRequestNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
