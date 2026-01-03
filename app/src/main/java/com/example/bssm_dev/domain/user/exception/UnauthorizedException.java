package com.example.bssm_dev.domain.user.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class UnauthorizedException extends GlobalException {

    private UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED_ACCESS);
    }

    public static UnauthorizedException raise() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final UnauthorizedException INSTANCE = new UnauthorizedException();
    }
}
