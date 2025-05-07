package com.project.Haru_Mail.api.diary;

import com.project.Haru_Mail.api.Tag.TagDto;
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
    public Diary createDiary(@RequestBody DiaryDto.DiaryRequestDto request) { // 일기+태그 저장
        System.out.println("Diary title: " + request.getDiary().getTitle());
        System.out.println("Diary content: " + request.getDiary().getContent());
        System.out.println("Diary userId: " + request.getDiary().getUserId());

        // 태그 리스트 출력
        for (TagDto.DiaryTagDto tag : request.getTags()) {
            System.out.println("Tag id: " + tag.getTagId());
        }

        return diaryService.createDiary(request);
    }
}
