package com.project.Haru_Mail.domain.user;

import com.project.Haru_Mail.api.user.UserDto.UserSettingRequest;
import com.project.Haru_Mail.common.jwt.JwtTokenizer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private JwtTokenizer jwtTokenizer;

    // 생성자에 JwtTokenizer를 추가로 주입
    public UserService(UserRepository userRepository, JwtTokenizer jwtTokenizer) {
        this.userRepository = userRepository;
        this.jwtTokenizer = jwtTokenizer;
    }

    // 사용자 저장
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // 요청의 쿠키에서 accessToken을 꺼내고, 검증 후 User를 조회해서 반환
    public User getCurrentUser(HttpServletRequest request) {
        String token = getTokenFromCookies(request);
        if (token == null) {
            throw new RuntimeException("No token found in cookies");
        }

        // JWT에서 이메일 뽑아내고
        String email = jwtTokenizer.getEmail(token);

        // DB 조회
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 쿠키에서 JWT 토큰 추출하는 메서드
    private String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();  // 쿠키 배열을 가져옵니다.
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {  // "accessToken"이라는 이름의 쿠키에서 토큰을 찾습니다.
                    return cookie.getValue();  // 해당 쿠키의 값을 반환
                }
            }
        }
        return null;  // 쿠키에 "accessToken"이 없으면 null 반환
    }

    public void updateUserSettings(UserSettingRequest request, HttpServletRequest httpRequest) {
        User user = getCurrentUser(httpRequest);

        int frequencyValue = switch (request.getSubscriptionFrequency()){
            case "daily" -> 1;
            case "every_other_day" -> 2;
            case "weekly" -> 7;
            default -> throw new IllegalArgumentException("잘못된 frequency 값입니다.");
        };

        user.setFrequency(frequencyValue);
        System.out.println(request.isSubscriptionAgreement());
        user.setAgreeToMail(request.isSubscriptionAgreement());

        userRepository.save(user);
    }
}