package com.project.Haru_Mail.common.config;

import com.project.Haru_Mail.common.jwt.JwtAuthenticationFilter;
import com.project.Haru_Mail.common.jwt.JwtTokenizer;
import com.project.Haru_Mail.domain.auth.CustomOAuth2UserService;
import com.project.Haru_Mail.domain.auth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenizer jwtTokenizer;
    private final CustomOAuth2UserService oauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session 설정
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/auth/verify/**", "/oauth2/authorization/**").permitAll()  // OAuth2 로그인 경로에 대해 접근 허용
                                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                .requestMatchers(CorsUtils::isCorsRequest).permitAll()
                                .anyRequest().authenticated()  // 그 외 요청은 인증 필요
                )
                .oauth2Login(oauth ->
                        oauth
                                .userInfoEndpoint(u -> u.userService(oauth2UserService))  // OAuth2 사용자 정보 처리 서비스 설정
                                .successHandler(oAuth2SuccessHandler)  // OAuth2 로그인 후 성공 핸들러 설정
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenizer),  // JWT 필터 추가
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class  // 인증 필터 전에 추가
                );
        return http.build();
    }
}