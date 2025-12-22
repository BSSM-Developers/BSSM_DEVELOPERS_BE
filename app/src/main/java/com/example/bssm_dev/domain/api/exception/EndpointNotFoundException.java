package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class EndpointNotFoundException extends GlobalException {
    private EndpointNotFoundException() {
        super(ErrorCode.ENDPOINT_NOT_FOUND);
    }

    private static class Holder {
        private static final EndpointNotFoundException INSTANCE = new EndpointNotFoundException();
    }

    public static EndpointNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
