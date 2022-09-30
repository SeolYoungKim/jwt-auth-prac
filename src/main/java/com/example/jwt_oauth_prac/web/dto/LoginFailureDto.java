package com.example.jwt_oauth_prac.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginFailureDto {

    private String id;
    private Boolean loginSuccess;
    private String message;

    private final String LOGIN_FAILURE_MESSAGE = "회원가입이 필요합니다.";

    @Builder
    public LoginFailureDto(String id, Boolean loginSuccess) {
        this.id = id;
        this.loginSuccess = loginSuccess;
        this.message = LOGIN_FAILURE_MESSAGE;
    }
}
