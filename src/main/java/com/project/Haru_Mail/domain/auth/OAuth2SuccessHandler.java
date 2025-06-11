package com.project.Haru_Mail.domain.auth;

import com.project.Haru_Mail.common.jwt.JwtTokenizer;
import com.project.Haru_Mail.domain.refreshToken.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private final JwtTokenizer jwtTokenizer;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // JWT 생성 후 쿠키에 저장
        updateTokensInCookies(response, email);

        log.info("Redis 저장 완료 : " + email);

        // 로그인 성공 후 리다이렉트 (사용자 정보는 URL에 포함하지 않음)
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:5173/list");
    }

    private void updateTokensInCookies(HttpServletResponse response, String email) {
        String accessToken = delegateAccessToken(email);  // Access Token 생성
        String refreshToken = delegateRefreshToken(email);  // Refresh Token 생성

        // Redis에 정보 저장
        saveTokensToRedis(email, refreshToken, accessToken);

        // Access Token을 쿠키에 저장
        addTokenToCookie(response, "accessToken", accessToken, 3600);

        // Refresh Token을 쿠키에 저장
        addTokenToCookie(response, "refreshToken", refreshToken, 14 * 24 * 60 * 60);
    }

    private void saveTokensToRedis(String email, String refreshToken, String accessToken) {
        RefreshToken token = new RefreshToken(refreshToken, email);
        redisTemplate.opsForValue().set(refreshToken, token, 7, TimeUnit.DAYS);
    }

    private void addTokenToCookie(HttpServletResponse response, String tokenName, String tokenValue, Integer time) {
        Cookie cookie = new Cookie(tokenName, tokenValue);
        cookie.setHttpOnly(false);  // JavaScript에서 접근 불가
        cookie.setSecure(false);  // HTTPS에서만 사용할지 말지 (테스트 환경에선 false로 설정)
        cookie.setPath("/");  // 전체 도메인에서 사용 가능
        cookie.setMaxAge(time);  // 1시간 동안 유효
        response.addCookie(cookie);
    }

    private String delegateAccessToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateAccessToken(claims, email, expiration, base64EncodedSecretKey);
    }

    private String delegateRefreshToken(String email) {
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateRefreshToken(email, expiration, base64EncodedSecretKey);
    }
}
