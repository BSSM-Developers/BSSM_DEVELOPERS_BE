package com.example.bssm_dev.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BSSM_EMAIL_INVALID(403, "bssm 이메일 형식이 아닙니다. (bssm.hs.kr)"),
    BASE64_CONVERSION_FAIL(500, "base64 변환을 실패하였습니다,")
    ;

    private final int statusCode;
    private final String errorMessage;
}
