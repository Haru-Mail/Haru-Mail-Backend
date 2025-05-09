package com.project.Haru_Mail.domain.diary;

import com.project.Haru_Mail.api.Tag.TagDto;
import com.project.Haru_Mail.api.Tag.TagDto.TagResponseDto;
import com.project.Haru_Mail.domain.category.Category;
import com.project.Haru_Mail.domain.category.CategoryRepository;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // 태그 생성
    public Tag createTag(TagDto.TagRequestDto requestDto) {
        // 카테고리 ID 고정: 6
        Category category = categoryRepository.findById(6)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 6번이 존재하지 않습니다."));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tag tag = new Tag();
        tag.setName(requestDto.getName());
        tag.setCategory(category);
        tag.setUser(user);

        return tagRepository.save(tag);
    }

    // 카테고리 ID로 태그 조회, 사용자 id로 커스텀 태그 걸러내야함
    public List<TagResponseDto> getTagsByCategory(Integer categoryId, Long userId) {
        List<Tag> tags;

        if (categoryId == 6) {
            // 사용자 커스텀 태그만
            tags = tagRepository.findByCategoryIdAndUserUserId(categoryId, userId);
        } else {
            // 시스템 기본 태그만
            tags = tagRepository.findByCategoryId(categoryId);
        }
        // 엔티티 -> DTO 반환
        return tags.stream()
                .map(tag -> new TagResponseDto(
                        tag.getId(),
                        tag.getName(),
                        tag.getCategory().getName()
                )).collect(Collectors.toList());
    }
}