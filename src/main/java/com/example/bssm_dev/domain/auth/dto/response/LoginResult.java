package com.example.bssm_dev.domain.auth.dto.response;

public sealed interface LoginResult permits LoginResult.LoginSuccess, LoginResult.SignupRequired {
    record LoginSuccess(String accessToken, String refreshToken) implements LoginResult {}
    record SignupRequired(String signupToken) implements LoginResult {}
}