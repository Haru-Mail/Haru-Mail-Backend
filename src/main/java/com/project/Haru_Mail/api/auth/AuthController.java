package com.project.Haru_Mail.api.auth;

import com.project.Haru_Mail.api.auth.AuthDto.UserInfo;
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
    public AuthDto.UserInfo getMe(HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        return new UserInfo(
                user.getEmail(),
                user.getUsername()
        );
    }
}
