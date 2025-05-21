package com.project.Haru_Mail.api.user;

import com.project.Haru_Mail.api.user.UserDto.UserSettingRequest;
import com.project.Haru_Mail.domain.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/setting")
    public ResponseEntity<Void> updateUserSettings(
            @RequestBody UserSettingRequest request,
            HttpServletRequest httpRequest
            ){
        userService.updateUserSettings(request, httpRequest);
        return ResponseEntity.ok().build();
    }
}
