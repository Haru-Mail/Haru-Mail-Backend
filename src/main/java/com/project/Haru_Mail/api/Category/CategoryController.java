package com.project.Haru_Mail.api.Category;

import com.project.Haru_Mail.domain.diary.Tag;
import com.project.Haru_Mail.domain.diary.TagService;
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

    @GetMapping("/{categoryId}/{userId}")
    public ResponseEntity<List<Tag>> getTagsByCategory(@PathVariable Integer categoryId, @PathVariable Long userId) {
        List<Tag> tags = tagService.getTagsByCategory(categoryId, userId);

        if (tags.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
