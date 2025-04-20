package com.project.Haru_Mail.api.auth;

import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    // 로그인된 사용자 정보를 반환하는 API
    @GetMapping("/me")
    public User getMe(HttpServletRequest request) {
        // 모든 로직은 UserService에 위임
        return userService.getCurrentUser(request);
    }
}
