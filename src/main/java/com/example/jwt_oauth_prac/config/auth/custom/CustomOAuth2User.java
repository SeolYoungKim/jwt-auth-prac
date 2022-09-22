package com.example.jwt_oauth_prac.config.auth.custom;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * 접속 요청을 한 유저에 대해 토큰이 만료되었는지 여부를 판단할 수 있는 기준이 필요하다.
 * 그래서 CustomOAuth2User를 만들어서 토큰 값을 받도록 해보자.
 */
public class CustomOAuth2User implements OAuth2User {

    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String name;  // AttibuteKey
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public CustomOAuth2User(Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities,
                            String name, String accessToken, String refreshToken) {
        this.attributes = attributes;
        this.authorities = authorities;
        this.name = name;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return name;
    }
}
