package com.example.jwt_oauth_prac.jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReissueRequestDto {
    private String refreshToken;

    @Builder
    public ReissueRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
