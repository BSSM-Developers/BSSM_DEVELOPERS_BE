package com.example.bssm_dev.global.util;


import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.global.error.ErrorResponse;

public class HttpUtil {

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(message, data);
    }

    public static <T> ResponseDto<T> success(String message) {
        return new ResponseDto<>(message, null);
    }

    public static ErrorResponse fail(int statusCode, String message) {
        return new ErrorResponse(statusCode, message);
    }
}
