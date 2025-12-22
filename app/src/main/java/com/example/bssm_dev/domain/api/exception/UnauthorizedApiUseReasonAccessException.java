package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.GlobalException;
import com.example.bssm_dev.exception.ErrorCode;

public class UnauthorizedApiUseReasonAccessException extends GlobalException {
    private UnauthorizedApiUseReasonAccessException() {
        super(ErrorCode.UNAUTHORIZED_API_USE_REASON_ACCESS);
    }

    private static class Holder {
        private static final UnauthorizedApiUseReasonAccessException INSTANCE = new UnauthorizedApiUseReasonAccessException();
    }

    public static UnauthorizedApiUseReasonAccessException raise() {
        return Holder.INSTANCE;
    }
}
