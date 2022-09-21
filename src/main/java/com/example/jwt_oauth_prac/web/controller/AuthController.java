package com.example.jwt_oauth_prac.web.controller;

import com.example.jwt_oauth_prac.jwt.dto.JwtDto;
import com.example.jwt_oauth_prac.sevice.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<JwtDto> login(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.ok(authService.login(oAuth2User));
    }
}
