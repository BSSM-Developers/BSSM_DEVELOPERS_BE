package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.GlobalException;
import com.example.bssm_dev.exception.ErrorCode;

public class UnauthorizedApiUsageAccessException extends GlobalException {
    private UnauthorizedApiUsageAccessException() {
        super(ErrorCode.UNAUTHORIZED_API_USAGE_ACCESS);
    }

    private static class Holder {
        private static final UnauthorizedApiUsageAccessException INSTANCE = new UnauthorizedApiUsageAccessException();
    }

    public static UnauthorizedApiUsageAccessException raise() {
        return Holder.INSTANCE;
    }
}
