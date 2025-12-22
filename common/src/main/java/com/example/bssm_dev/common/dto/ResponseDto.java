package com.example.bssm_dev.common.dto;

public record ResponseDto<T>(
        String message,
        T data
) {

}
