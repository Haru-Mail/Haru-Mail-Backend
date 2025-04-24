package com.project.Haru_Mail.domain.auth;

import com.project.Haru_Mail.common.jwt.JwtTokenizer;
import com.project.Haru_Mail.domain.refreshToken.RefreshToken;
import com.project.Haru_Mail.domain.refreshToken.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;

    public String reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // 1) 쿠키에서 refreshToken 꺼내기
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No RefreshToken"));

        // 2) RefreshToken 유효성 검사
        if (!jwtTokenizer.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid RefreshToken");
        }

        // 3) Redis에서 accessToken으로 엔티티 조회
        RefreshToken tokenEntity = refreshTokenService.findByAccessToken(refreshToken);

        // 4) email 꺼내기
        String email = tokenEntity.getEmail();

        // 5) 새로운 AccessToken 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        String base64Key = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Date exp = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String newAccessToken = jwtTokenizer.generateAccessToken(claims, email, exp, base64Key);

        // 6) Redis에 업데이트
        tokenEntity.setAccessToken(newAccessToken);
        refreshTokenService.save(email, tokenEntity.getRefreshToken(), newAccessToken);

        // 7) 쿠키에도 설정
        Cookie cookie = new Cookie("accessToken", newAccessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtTokenizer.getAccessTokenExpirationMinutes() * 60);
        response.addCookie(cookie);

        return newAccessToken;
    }
}
