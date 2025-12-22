package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class UnsupportedProxyBasePathException extends GlobalException {
    public UnsupportedProxyBasePathException() {
        super(ErrorCode.UNSUPPORTED_PROXY_BASE_PATH);
    }

    public static UnsupportedProxyBasePathException raise() {
        return new UnsupportedProxyBasePathException();
    }
}
