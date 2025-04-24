package com.project.Haru_Mail.api.diary;

import com.project.Haru_Mail.domain.diary.Diary;
import com.project.Haru_Mail.domain.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping
    public Diary createDiary(@RequestBody DiaryDto.DiaryRequestDto request) {
        return diaryService.createDiary(request);
    }
}
