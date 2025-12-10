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
    DOCS_SECTION_NOT_FOUND(404, "문서 섹션을 찾을 수 없습니다."),
    DOCS_PAGE_NOT_FOUND(404, "문서 페이지를 찾을 수 없습니다."),
    DOCS_SECTION_MISMATCH(400, "해당 섹션은 이 문서에 속하지 않습니다."),
    DOCS_PAGE_MISMATCH(400, "해당 페이지는 이 섹션에 속하지 않습니다."),
    UNAUTHORIZED_DOCS_ACCESS(403, "해당 문서에 접근할 권한이 없습니다."),
    API_NOT_FOUND(404, "API를 찾을 수 없습니다."),
    API_TOKEN_NOT_FOUND(404, "API 토큰을 찾을 수 없습니다."),
    INVALID_SECRET_KEY(401, "유효하지 않은 시크릿 키입니다."),
    UNAUTHORIZED_DOMAIN(403, "허용되지 않은 도메인에서의 요청입니다."),
    ENDPOINT_NOT_FOUND(404, "존재하지 않는 엔드포인트입니다."),
    EXTERNAL_API_ERROR(502, "API 호출 중 오류가 발생했습니다."),
    API_USE_REASON_NOT_FOUND(404, "API 사용 신청을 찾을 수 없습니다."),
    UNAUTHORIZED_API_TOKEN_ACCESS(403, "해당 API 토큰에 접근할 권한이 없습니다."),
    UNAUTHORIZED_API_USE_REASON_ACCESS(403, "해당 API 사용 신청에 접근할 권한이 없습니다."),
    API_USAGE_NOT_FOUND(404, "API 사용을 찾을 수 없습니다."),
    API_USAGE_ALREADY_EXISTS(400, "이미 해당 API에 대한 사용 신청이 되어있습니다."),
    API_USAGE_ENDPOINT_ALREADY_EXISTS(400, "이미 동일한 엔드포인트가 존재합니다."),
    UNAUTHORIZED_API_USAGE_ACCESS(403, "해당 API 사용에 접근할 권한이 없습니다."),
    INVALID_API_USE_REASON_STATE(400, "API 사용 신청 이유 상태 변환(문자열 -> ENUM)에 실패했습니다. (허용된 값: PENDING, APPROVED, REJECTED, ALL)"),
    INVALID_DOCS_TYPE_VALUE(400, "문서 타입 변환(문자열 -> ENUM)에 실패했습니다. (허용된 값: CUSTOMIZE, ORIGINAL)"),
    DOCS_PAGE_NOT_API_PAGE(400, "해당 페이지는 API 페이지가 아닙니다."),
    DOCS_API_DOCUMENT_NOT_FOUND(404, "API 문서(요청, 응답)을 찾을 수 없습니다."),
    DOCS_NOT_CUSTOM_TYPE(400, "해당 문서는 커스텀 타입이 아닙니다."),
    DOCS_SECTION_REQUIRED(400, "문서에는 최소 1개 이상의 섹션이 필요합니다."),
    DOCS_SIDEBAR_NOT_FOUND(404, "문서 사이드바를 찾을 수 없습니다.");


    private final int statusCode;
    private final String errorMessage;
}

