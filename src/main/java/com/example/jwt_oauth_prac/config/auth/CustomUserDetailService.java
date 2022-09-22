package com.example.jwt_oauth_prac.config.auth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailService extends DefaultOAuth2UserService {

    /**
     * 로그인을 한 유저의 정보를 이용해서 -> UserDetails(Authentication)을 커스텀해서 반환해줄 수 있는 로직이다.
     * 여기서 처리를 좀 해서, DefaultOAuth2User를 만들어주자. 안 만들어주면 기본 제공 유저 말고는, 나머지 파싱이 안된다.
     * 여기서 처리하고, 로그인한 유저를 좀 평준화 해서 돌려주면 -> 나머지 로직에서 처리가 될 것이다.
     * TODO: 여기서 현재 접속한 유저의 UserDetails를 OAuth2User의 형태로 반환을 한다.
     * 그렇기 때문에, 여기서 기존 회원의 토큰이나 정보를 비교하는 로직이 필요할 것 같다. 비교해서 접속한 유저의 정보에 맞는 값을 반환해줘야 하기 때문이다.
     * CustomOAuth2User 객체를 만들어서, 토큰을 가진 형태의 UserDetails를 만들 수는 없을까? 한번 해보자.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // google, naver, kakao, github 이라는 이름이 될 아이디들
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        // 유저의 이름이나 식별자에 접근할 때 사용함
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 로그인한 OAuth2 유저를 공통 key를 가진 attr을 넣어주고 반환해주기 위함.
        // "sub" = "email주소", ... 이런식으로 key값을 전부 통일
        OAuthAttributes attr
                = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        //TODO: 여기서 접속요청한 유저의 email주소를 기반으로 유저를 찾는다. 회원가입 돼있으면 기성 정보를 이용해서 권한과 토큰값등을 할당한다.
        // 토큰이 만료되었을 수도 있다.(기간 확인) -> 그렇다면 토큰을 재발급 해주는 로직이 필요하다. 여기서 하는게 맞나?
        // 아니면 필터를 하나 추가해서 거기서 작업하고 여기로 값을 넘겨주는게 나을까? 공부를 더 해보자.
        // 회원가입이 안되어있을 경우, 새로 토큰을 발급해준다.
        // findByEmail(이메일주소)...

        //TODO: DefaultOAuth2User의 경우, 토큰 값을 불러올 수 없다는 단점이 있다.
        // 이럴 경우, CustomOAuth2User객체를 이용해서 기존 정보 혹은 새 정보를 넣어줌으로써 UserDetails(혹은 Authentication) 객체를 만든다.

        // 로그인을 한 유저의 정보를 다음과 같이 반환하는 것임.
        // -> 커스터마이징 해서, 기존의 유저 정보에서 토큰 정보를 불러와야함 혹은 신규 유저일 경우 토큰을 추가해주는 로직을 수행해야 함
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attr.parsedAttributes(),
                attr.getNameAttributeKey()
        );
    }
}
