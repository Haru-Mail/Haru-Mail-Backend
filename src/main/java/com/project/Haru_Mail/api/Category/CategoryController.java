package com.project.Haru_Mail.api.Category;

import com.project.Haru_Mail.api.Tag.TagDto.TagResponseDto;
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
    public ResponseEntity<List<TagResponseDto>> getTagsByCategory(@PathVariable Integer categoryId,
                                                                  HttpServletRequest request) {
        User currentUser = userService.getCurrentUser(request);
        List<TagResponseDto> tags = tagService.getTagsByCategory(categoryId, currentUser.getUserId());

        if (tags.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
