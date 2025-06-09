package com.project.Haru_Mail.domain.diary;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DiaryTagService {

    private final DiaryTagRepository diaryTagRepository;
    private final DiaryRepository diaryRepository;

    public DiaryTagService(DiaryTagRepository diaryTagRepository, DiaryRepository diaryRepository) {
        this.diaryTagRepository = diaryTagRepository;
        this.diaryRepository = diaryRepository;
    }

    // 태그 기반 검색
    public List<Diary> searchDiary(Long userId, List<Integer> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 모든 태그를 포함하는 diaryId 목록 조회
        List<Integer> diaryIds = diaryTagRepository.findDiaryIdsWithAllTags(tagIds, tagIds.size());

        if (diaryIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 해당 diaryId 중 userId가 작성한 것만 필터링
        List<Diary> diaryList =  diaryRepository.findByIdInAndUserUserId(diaryIds, userId);

        for (Diary diary : diaryList) { // 디버깅용
            System.out.println("User ID: " + diary.getUser().getUserId() + ", Title: " + diary.getTitle());
        }

        return diaryList;
    }
}
