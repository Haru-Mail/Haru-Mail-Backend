package com.project.Haru_Mail.domain.auth;

import com.project.Haru_Mail.common.jwt.JwtTokenizer;
import com.project.Haru_Mail.domain.refreshToken.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenizer jwtTokenizer;
    private final RedisTemplate<String, RefreshToken> redisTemplate;

    public String reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // 1. 쿠키 유무 확인
        if (request.getCookies() == null) {
            throw new RuntimeException("No cookies found");
        }

        // 2. RefreshToken 추출
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No RefreshToken"));

        // 3. RefreshToken 유효성 검사
        if (!jwtTokenizer.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid RefreshToken");
        }

        // 4. Redis 조회
        RefreshToken tokenEntity = redisTemplate.opsForValue().get(refreshToken);
        if (tokenEntity == null) {
            throw new RuntimeException("RefreshToken not found in Redis");
        }

        // 5. Email 추출 및 검증
        String email = tokenEntity.getEmail();
        if (!jwtTokenizer.getEmail(refreshToken).equals(email)) {
            throw new RuntimeException("Token email mismatch");
        }

        // 6. AccessToken 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        String base64Key = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Date exp = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String newAccessToken = jwtTokenizer.generateAccessToken(claims, email, exp, base64Key);

        // 7. 쿠키에 저장
        addTokenToCookie(response, "accessToken", newAccessToken, jwtTokenizer.getAccessTokenExpirationMinutes() * 60);

        return newAccessToken;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 쿠키에서 refreshToken 꺼내기
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (refreshToken != null && jwtTokenizer.validateToken(refreshToken)) {
            // 2. Redis에서 해당 토큰 엔티티 조회 및 삭제
            RefreshToken tokenEntity = redisTemplate.opsForValue().get(refreshToken);
            if (tokenEntity != null) {
                redisTemplate.delete(refreshToken); // Redis에서 삭제
            }
        }

        // 3. 클라이언트 쿠키 삭제
        deleteCookie("accessToken", response);
        deleteCookie("refreshToken", response);
    }

    private void addTokenToCookie(HttpServletResponse response, String tokenName, String tokenValue, Integer maxAge) {
        Cookie cookie = new Cookie(tokenName, tokenValue);
        cookie.setHttpOnly(false);  // JavaScript에서 접근 불가
        cookie.setSecure(false);  // HTTPS에서만 사용할지 말지 (실제 서비스 환경에서는 true로 설정)
        cookie.setPath("/");  // 전체 도메인에서 사용 가능
        cookie.setMaxAge(maxAge);  // 유효 시간 설정
        String sameSiteValue = "None"; // Cross-Origin 상황에서 SameSite=None 사용
        String cookieHeader = String.format("%s=%s; Max-Age=%d; Path=%s; HttpOnly; Secure; SameSite=None",
                tokenName,
                tokenValue,
                maxAge,
                cookie.getPath());
        response.addHeader("Set-Cookie", cookieHeader);
    }

    private void deleteCookie(String tokenName, HttpServletResponse response) {
        Cookie cookie = new Cookie(tokenName, null);
        cookie.setHttpOnly(false);  // JavaScript에서 접근 불가
        cookie.setSecure(false);  // HTTPS에서만 사용할지 말지 (실제 서비스 환경에서는 true로 설정)
        cookie.setPath("/");  // 전체 도메인에서 사용 가능
        cookie.setMaxAge(0);  // 즉시 만료
        String sameSiteValue = "None"; // 설정 시 사용했던 SameSite 값과 동일하게 지정
        String cookieHeader = String.format("%s=%s; Max-Age=%d; Path=%s; HttpOnly; Secure; SameSite=%s",
                tokenName,
                "", // 삭제 시에는 값을 빈 문자열로 보내는 것이 일반적입니다.
                0,  // Max-Age를 0으로 설정
                cookie.getPath(), // "/"
                sameSiteValue);
        response.addHeader("Set-Cookie", cookieHeader);
    }
}
