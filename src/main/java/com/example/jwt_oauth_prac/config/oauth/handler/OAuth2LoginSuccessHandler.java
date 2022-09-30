package com.example.jwt_oauth_prac.config.oauth.handler;

import com.example.jwt_oauth_prac.jwt.JwtProvider;
import com.example.jwt_oauth_prac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // OAuth2 로그인 성공 시에는 회원 여부를 판단해야 한다.
        OAuth2User user = (OAuth2User) authentication.getPrincipal();  // 현재 OAuth2 인증 유저
        Map<String, Object> attributes = user.getAttributes();

        String authId = (String) attributes.get("id");  // authId를 가져온다. 이걸로 회원가입 여부를 판단할 수 있다.

        if (!memberRepository.existsByAuthId(authId)) {  // 리포지토리에 없으면 최초 로그인 회원이다.
//            response.sendRedirect("/api/test/login-failure");
            response.sendRedirect("http://localhost:8082/hi");
        } else {

            String jwt = jwtProvider.createJwt(user);
            response.setHeader("Authentication", jwt);
            response.sendRedirect("/api/test/login-success");
        }

    }
}
