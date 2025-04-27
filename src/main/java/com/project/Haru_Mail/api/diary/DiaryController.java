package com.project.Haru_Mail.api.diary;

import com.project.Haru_Mail.domain.diary.Diary;
import com.project.Haru_Mail.domain.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/save")
    public Diary createDiary(@RequestBody DiaryDto.NewDiaryDto request) { //나중에 DiaryDto.DiaryRequestDto request로 변경
        return diaryService.createDiary(request);
    }
}
