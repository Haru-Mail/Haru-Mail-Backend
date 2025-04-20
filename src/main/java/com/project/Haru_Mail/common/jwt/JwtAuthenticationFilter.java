package com.project.Haru_Mail.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        // 쿠키에서 JWT 토큰 추출
        Cookie[] cookies = req.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {  // 쿠키 이름은 "accessToken"
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 토큰이 존재하고 유효한 경우
        if (token != null && jwtTokenizer.validateToken(token)) {
            String email = jwtTokenizer.getEmail(token);  // JWT에서 이메일 추출
            var auth = new UsernamePasswordAuthenticationToken(email, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);  // 인증 정보 설정
        }

        // 필터 체인 진행
        chain.doFilter(req, res);
    }
}
