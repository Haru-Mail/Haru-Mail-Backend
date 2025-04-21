package com.project.Haru_Mail.domain.auth;

import com.project.Haru_Mail.common.jwt.JwtTokenizer;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepo;
    private final JwtTokenizer jwtTokenizer;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(req);

        // 구글이 반환하는 'sub' 속성: 구글 고유 ID
        String googleId = oAuth2User.<String>getAttribute("sub");
        String email    = oAuth2User.<String>getAttribute("email");
        String name     = oAuth2User.getAttribute("name");

        // DB 저장 or 조회
        userRepo.findByGoogleId(googleId)
                .orElseGet(() -> userRepo.save(
                        User.builder()
                                .googleId(googleId)
                                .email(email)
                                .username(name) // 사용자 이름을 기본 닉네임으로 설정
                                .frequency(7)  // 메일 발송 빈도 기본값은 7일
                                .build()
                ));
        // JWT 토큰 생성
        String token = jwtTokenizer.createToken(email);

        // 기존 attributes에 토큰 추가
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("token", token);

        // 반환
        return new DefaultOAuth2User(
                Collections.emptyList(), // 권한 정보 추가 필요시 수정
                attributes, "email" // 주 식별자 key
        );
    }
}