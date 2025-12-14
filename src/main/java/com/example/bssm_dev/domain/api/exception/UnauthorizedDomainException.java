package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class UnauthorizedDomainException extends GlobalException {
    private UnauthorizedDomainException() {
        super(ErrorCode.UNAUTHORIZED_DOMAIN);
    }

    private static class Holder {
        private static final UnauthorizedDomainException INSTANCE = new UnauthorizedDomainException();
    }

    public static UnauthorizedDomainException raise() {
        return Holder.INSTANCE;
    }
}
