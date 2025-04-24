package com.project.Haru_Mail.api.Tag;

import com.project.Haru_Mail.domain.diary.Tag;
import com.project.Haru_Mail.domain.diary.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    // 태그 생성 API
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody TagDto.TagRequestDto requestDto) {
        Tag tag = tagService.createTag(requestDto);  // 서비스에서 태그 생성
        return new ResponseEntity<>(tag, HttpStatus.CREATED);  // 생성된 태그 반환 or 아이디만 반환
    }

    // 카테고리 ID로 태그 목록 조회, category로 옮길까?..
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Tag>> getTagsByCategory(@RequestBody TagDto.ShowTagDto showTagDto) {
        List<Tag> tags = tagService.getTagsByCategory(showTagDto);
        if (tags.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 태그가 없으면 204 No Content 반환
        }
        return new ResponseEntity<>(tags, HttpStatus.OK);  // 태그가 있으면 200 OK와 함께 반환
    }
}
