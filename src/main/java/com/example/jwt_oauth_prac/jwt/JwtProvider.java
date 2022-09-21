package com.example.jwt_oauth_prac.jwt;

import com.example.jwt_oauth_prac.jwt.dto.JwtDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import static java.util.Objects.*;

@Slf4j
@Component
public class JwtProvider {

    private final static String AUTHORITIES_KEY = "auth";
    private final static String BEARER_TYPE = "bearer";

    @Value("${jwt.access-token-expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refresh-token-expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 현재 로그인한 유저의 정보를 기반으로, Jwt 토큰을 생성 -> 얘를 이제 Member에 저장하고 관리하는 등의 로직이 필요!
    public JwtDto generateJwtDto(OAuth2User oAuth2User) {
        long now = new Date().getTime();
        Date accessTokenExpireTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String authorities = oAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setSubject(requireNonNull(oAuth2User.getAttribute("sub")).toString())  // "sub" : "email"
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpireTime)  // "exp" : 시간
                .signWith(key, SignatureAlgorithm.HS512)  // "alg" : "HS512"
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return JwtDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireTime(accessTokenExpireTime.getTime())
                .build();
    }


}

