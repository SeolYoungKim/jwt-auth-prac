package com.example.jwt_oauth_prac.config.oauth.handler.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginSuccessDto {

    private String id;
    private String email;
    private String name;
    private String picture;
    private Boolean loginSuccess;
    private String accessToken;

    @Builder
    public LoginSuccessDto(String id, String email, String name, String picture, Boolean loginSuccess, String accessToken) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.loginSuccess = loginSuccess;
        this.accessToken = accessToken;
    }
}
