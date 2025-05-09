package com.project.Haru_Mail.api.mailing;

import com.project.Haru_Mail.domain.mailing.MailingService;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class MailingController {
    private final MailingService mailingService;
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<String> sendTestMail(HttpServletRequest request) throws Exception {
        User currentUser = userService.getCurrentUser(request);
        mailingService.sendDailyMailToAllUsers();

        return new ResponseEntity<>("테스트 메일 발송 요청 성공", HttpStatus.OK);
    }

    @PostMapping("/settings")
    public ResponseEntity<Void> settingFrequency(@RequestBody Map<String, String> body,
                                                 HttpServletRequest request) throws Exception {
        User currentUser = userService.getCurrentUser(request);
        mailingService.settingMailingFrequency(currentUser, body);
        return ResponseEntity.ok().build();
    }
}
