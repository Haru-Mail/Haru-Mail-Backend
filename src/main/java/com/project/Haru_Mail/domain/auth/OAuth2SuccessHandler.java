package com.project.Haru_Mail.domain.auth;

import com.project.Haru_Mail.common.jwt.JwtTokenizer;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        // JWT 생성 후 쿠키에 저장
        storeTokensInCookies(response, email);

        // 로그인 성공 후 리다이렉트 (사용자 정보는 URL에 포함하지 않음)
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:63342/Haru-Mail/src/main/resources/templates/success.html");
    }

    private void storeTokensInCookies(HttpServletResponse response, String email) {
        String accessToken = delegateAccessToken(email);  // Access Token 생성
        String refreshToken = delegateRefreshToken(email);  // Refresh Token 생성

        saveUser(email, refreshToken);

        // Access Token을 쿠키에 저장
        addTokenToCookie(response, "accessToken", accessToken);
    }

    private void saveUser(String email, String refreshToken){
        User user = userService.findByEmail(email);  // 이메일로 사용자 찾기
        user.setRefreshToken(refreshToken);  // 사용자에 리프레시 토큰 저장
        userService.saveUser(user);  // 사용자 정보 저장
    }

    private void addTokenToCookie(HttpServletResponse response, String tokenName, String tokenValue) {
        Cookie cookie = new Cookie(tokenName, tokenValue);
        cookie.setHttpOnly(true);  // JavaScript에서 접근 불가
        cookie.setSecure(false);  // HTTPS에서만 사용할지 말지 (테스트 환경에선 false로 설정)
        cookie.setPath("/");  // 전체 도메인에서 사용 가능
        cookie.setMaxAge(3600);  // 1시간 동안 유효
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
        String subject = email;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
    }
}
