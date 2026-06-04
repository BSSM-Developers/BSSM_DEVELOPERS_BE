package com.example.bssm_dev.domain.auth.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class ExpiredTokenException extends GlobalException {

    private ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }

    static class Holder {
        private static final ExpiredTokenException INSTANCE = new ExpiredTokenException();
    }

    public static ExpiredTokenException raise() {
        return Holder.INSTANCE;
    }
}