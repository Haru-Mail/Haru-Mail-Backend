package com.project.Haru_Mail.api.diary;

import com.project.Haru_Mail.domain.diary.Diary;
import com.project.Haru_Mail.domain.diary.DiaryService;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    private final UserService userService;

    @PostMapping("/save")
    public Diary createDiary(@RequestBody DiaryDto.NewDiaryDto request,
                             HttpServletRequest httpServletRequest) { //나중에 DiaryDto.DiaryRequestDto request로 변경
        User currentUser = userService.getCurrentUser(httpServletRequest);
        return diaryService.createDiary(request, currentUser);
    }
}
