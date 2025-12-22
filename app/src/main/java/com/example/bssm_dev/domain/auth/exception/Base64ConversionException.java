package com.example.bssm_dev.domain.auth.exception;


import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class Base64ConversionException extends GlobalException {
    public Base64ConversionException() {
        super(ErrorCode.BASE64_CONVERSION_FAIL);
    }

    static class Holder {
        private final static Base64ConversionException INSTANCE = new Base64ConversionException();
    }

    public static Base64ConversionException raise() {
        return Holder.INSTANCE;
    }
}
