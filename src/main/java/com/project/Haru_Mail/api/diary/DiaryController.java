package com.project.Haru_Mail.api.diary;

import com.project.Haru_Mail.api.Tag.TagDto;
import com.project.Haru_Mail.domain.diary.Diary;
import com.project.Haru_Mail.domain.diary.DiaryService;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;
    private final UserService userService;

    @PostMapping("/save")
    public Diary createDiary(@RequestBody DiaryDto.DiaryRequestDto request,
                             HttpServletRequest httpServletRequest) { // 일기+태그 저장
        User currentUser = userService.getCurrentUser(httpServletRequest);
        System.out.println("Diary title: " + request.getDiary().getTitle());
        System.out.println("Diary content: " + request.getDiary().getContent());
        System.out.println("Diary userId: " + request.getDiary().getUserId());

        // 태그 리스트 출력
        for (TagDto.DiaryTagDto tag : request.getTags()) {
            System.out.println("Tag id: " + tag.getTagId());
        }

        return diaryService.createDiary(request,currentUser);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDto.DiaryInfoDto> LoadDiaryInfo(@PathVariable Integer diaryId) {
        DiaryDto.DiaryInfoDto diary = diaryService.getDiaryInfo(diaryId); // 일기 상세정보

        System.out.println("Diary title: " + diary.getTitle());
        System.out.println("Diary title: " + diary.getContent());

        for (String tag : diary.getTags()) {
            System.out.println("Tag id: " + tag);
        }

        return new ResponseEntity<>(diary, HttpStatus.OK);
    }
}
