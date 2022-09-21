package com.example.jwt_oauth_prac.domain;

import lombok.Getter;

@Getter
public enum RoleType {

    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "일반 유저");

    private final String key;
    private final String desc;

    RoleType(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
