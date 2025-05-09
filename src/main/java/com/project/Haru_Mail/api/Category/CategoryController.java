package com.project.Haru_Mail.api.Category;

import com.project.Haru_Mail.api.Tag.TagDto;
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
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final TagService tagService;
    private final UserService userService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<TagDto.LoadTagDto>> getTagsByCategory(@PathVariable Integer categoryId,
                                                                     HttpServletRequest request) {
        User currentUser = userService.getCurrentUser(request);

        List<Tag> tags = tagService.getTagsByCategory(categoryId, currentUser);

        // tags 출력
        System.out.println("불러온 태그 목록:");
        for (Tag tag : tags) {
            System.out.println(tag.getName());
        }

        // tags를 DTO로 변환
        List<TagDto.LoadTagDto> dtoList = tags.stream()
                .map(tag -> new TagDto.LoadTagDto(tag.getId(), tag.getName()))
                .toList();


        if (tags.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
