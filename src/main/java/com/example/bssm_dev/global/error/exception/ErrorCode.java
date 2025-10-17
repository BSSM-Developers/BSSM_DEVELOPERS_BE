package com.example.bssm_dev.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BSSM_EMAIL_INVALID(403, "bssm 이메일 형식이 아닙니다. (bssm.hs.kr)"),
    BASE64_CONVERSION_FAIL(500, "base64 변환을 실패하였습니다,"),
    SIGNUP_REQUEST_ALREADY_EXISTS(400, "이미 회원가입 신청이 되어있습니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(401, "리프레시 토큰을 찾을 수 없습니다."),
    INVALID_STATE_PARAMETER(400, "유효하지 않은 state 파라미터입니다."),
    SIGNUP_REQUEST_NOT_FOUND(404, "회원가입 신청을 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    INVALID_SIGNUP_TOKEN(401, "유효하지 않은 회원가입 토큰입니다."),
    UNAUTHORIZED_SIGNUP_ACCESS(403, "해당 회원가입 신청에 접근할 권한이 없습니다."),
    INVALID_SIGNUP_STATE(400, "유효하지 않은 회원가입 상태입니다."),
    DOCS_NOT_FOUND(404, "문서를 찾을 수 없습니다."),
    UNAUTHORIZED_DOCS_ACCESS(403, "해당 문서에 접근할 권한이 없습니다."),
    API_NOT_FOUND(404, "API를 찾을 수 없습니다."),
    API_TOKEN_NOT_FOUND(404, "API 토큰을 찾을 수 없습니다."),
    API_USE_REASON_NOT_FOUND(404, "API 사용 신청을 찾을 수 없습니다."),
    UNAUTHORIZED_API_TOKEN_ACCESS(403, "해당 API 토큰에 접근할 권한이 없습니다."),
    UNAUTHORIZED_API_USE_REASON_ACCESS(403, "해당 API 사용 신청에 접근할 권한이 없습니다."),
    API_USAGE_NOT_FOUND(404, "API 사용을 찾을 수 없습니다."),
    UNAUTHORIZED_API_USAGE_ACCESS(403, "해당 API 사용에 접근할 권한이 없습니다.")
    ;

    private final int statusCode;
    private final String errorMessage;
}
