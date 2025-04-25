package com.project.Haru_Mail.domain.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    // 카테고리 ID로 태그를 조회
    List<Tag> findByCategoryId(Integer categoryId);
    List<Tag> findByCategoryIdAndUserUserId(Integer categoryId, Long userId);
}
