package com.project.Haru_Mail.api.auth;

import com.project.Haru_Mail.api.auth.AuthDto.UserInfo;
import com.project.Haru_Mail.domain.auth.AuthService;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserRepository;
import com.project.Haru_Mail.domain.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final UserRepository userRepository;

    // 로그인된 사용자 정보를 반환하는 API
    @GetMapping("/me")
    public AuthDto.UserInfo getMe(HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        return new UserInfo(
                user.getEmail(),
                user.getUsername()
        );
    }

    // AccessToken 재발급 API
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response){
        String accessToken = authService.reissueAccessToken(request, response);
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        authService.logout(request, response);
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifySubscriber(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(Map.of("status", "verified"));
        } else {
            // 인증 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
        }
    }
}
