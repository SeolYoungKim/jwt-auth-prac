package com.example.jwt_oauth_prac.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestDto {

    private String code;
    private String state;

    @Builder
    public RequestDto(String code, String state) {
        this.code = code;
        this.state = state;
    }
}
