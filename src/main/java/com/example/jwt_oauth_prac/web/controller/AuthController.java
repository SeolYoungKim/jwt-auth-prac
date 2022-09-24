package com.example.jwt_oauth_prac.web.controller;

import com.example.jwt_oauth_prac.jwt.dto.JwtDto;
import com.example.jwt_oauth_prac.jwt.dto.ReissueRequestDto;
import com.example.jwt_oauth_prac.sevice.AuthService;
import com.example.jwt_oauth_prac.web.dto.RequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Value("${oauth.naver.client-id}")
    private String clientIdNaver;

    @Value("${oauth.naver.client-secret}")
    private String clientSecretNaver;

    @GetMapping("/login")
    public ResponseEntity<JwtDto> login(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.ok(authService.login(oAuth2User));
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> reissue(@RequestBody ReissueRequestDto reissueRequestDto) {
        return ResponseEntity.ok(authService.reissue(reissueRequestDto));
    }

    @PostMapping("/test")
    public String test1(@RequestBody RequestDto naverDto) {
        // ㅇㅕ기서도 받아온 값을 네이버에 POST로 보내고, 정보를 받아오는 것 까지 해보자.
        log.info("code = {}", naverDto.getCode());
        log.info("state = {}", naverDto.getState());

        WebClient webClient = WebClient.create();

        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("nid.naver.com")
                .path("/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientIdNaver)
                .queryParam("client_secret", clientSecretNaver)
                .queryParam("code", naverDto.getCode())
                .queryParam("state", naverDto.getState())
                .build();


        // 받아온 값을 네이버에 쏴준다. url 형식을 지켜서!
        // code랑 state를 전달 받는것까지는 되는데, 이 아래의 것을 수행하려고하면 안됨ㅋㅋ ㅠㅠ
        return webClient.get()
                .uri(uri.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }
}
