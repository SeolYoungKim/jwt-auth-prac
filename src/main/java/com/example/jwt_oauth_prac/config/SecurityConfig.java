package com.example.jwt_oauth_prac.config;

import com.example.jwt_oauth_prac.config.oauth.handler.OAuth2LoginSuccessHandler;
import com.example.jwt_oauth_prac.config.oauth.service.CustomUserDetailService;
import com.example.jwt_oauth_prac.domain.RoleType;
import com.example.jwt_oauth_prac.jwt.JwtFilter;
import com.example.jwt_oauth_prac.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final JwtProvider jwtProvider;
    private final OAuth2LoginSuccessHandler loginSuccessHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(
                "/h2-console/**",
                "/favicon.ico",
                "/error"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)

                .and()
                .authorizeRequests(request -> request
                        .antMatchers("/auth/reissue").permitAll()
                        .antMatchers("/guest").hasRole(RoleType.USER.name())
                        .antMatchers("/admin").hasRole(RoleType.ADMIN.name()))
                .oauth2Login(login -> login
                        .userInfoEndpoint()
                        .userService(customUserDetailService)
                        .and()
                        .successHandler(loginSuccessHandler));
//                        .defaultSuccessUrl("/api/auth/login")
//                        .failureUrl("/fail"));
//                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
