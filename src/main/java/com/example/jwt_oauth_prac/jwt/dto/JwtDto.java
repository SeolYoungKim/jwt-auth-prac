package com.example.jwt_oauth_prac.jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireTime;

    @Builder
    public JwtDto(String grantType, String accessToken, String refreshToken, Long accessTokenExpireTime) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireTime = accessTokenExpireTime;
    }
}
