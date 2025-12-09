package com.example.bssm_dev.domain.api.model;

public enum TokenType {
    SERVER,   // 서버-투-서버, 시크릿 키 기반 인증
    BROWSER   // 브라우저-투-서버, 도메인 기반 인증
}
