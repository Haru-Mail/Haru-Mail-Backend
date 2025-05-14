package com.project.Haru_Mail.domain.diary;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryTagRepository extends JpaRepository<DiaryTag, DiaryTagId> {
    @Query(value = """
        SELECT dt.diary_id
        FROM Diary_Tag dt
        WHERE dt.tag_id IN (:tagIds)
        GROUP BY dt.diary_id
        HAVING COUNT(DISTINCT dt.tag_id) = :size
        """, nativeQuery = true)
    List<Integer> findDiaryIdsWithAllTags(@Param("tagIds") List<Integer> tagIds, @Param("size") int size);

    List<DiaryTag> findByDiaryId(Integer diaryId);
}