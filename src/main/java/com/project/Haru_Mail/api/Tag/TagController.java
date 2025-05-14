package com.project.Haru_Mail.api.Tag;

import com.project.Haru_Mail.domain.diary.Diary;
import com.project.Haru_Mail.domain.diary.DiaryTagService;
import com.project.Haru_Mail.domain.diary.Tag;
import com.project.Haru_Mail.domain.diary.TagService;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final UserService userService;
    private final DiaryTagService diaryTagService;

    // 태그 생성 API
    @PostMapping("/create")
    public ResponseEntity<Tag> createTag(@RequestBody TagDto.TagRequestDto requestDto,
                                         HttpServletRequest httpServletRequest) {
        User currentUser = userService.getCurrentUser(httpServletRequest);
        Tag tag = tagService.createTag(requestDto, currentUser);  // 서비스에서 태그 생성
        return new ResponseEntity<>(tag, HttpStatus.CREATED); // 생성된 태그 반환 or 아이디만 반환
    }

}
