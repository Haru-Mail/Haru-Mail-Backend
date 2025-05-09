package com.project.Haru_Mail.api.diary;

import com.project.Haru_Mail.api.Tag.TagDto;
import com.project.Haru_Mail.domain.diary.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class DiaryDto {
    @Data
    @AllArgsConstructor
    public static class NewDiaryDto {
        private Integer id;
        private String title; // 제목
        private String content; // Editor.js로 작성된 JSON
        private Long userId; // 사용자 ID
    }

    @Data
    @AllArgsConstructor
    public static class DiaryRequestDto { // 일기 생성
        private NewDiaryDto diary; // 일기 정보
        private List<TagDto.DiaryTagDto> tags; // 태그 리스트
    }

    @Data
    @AllArgsConstructor
    public static class DiaryInfoDto { // 일기 상세정보
        private Integer diaryId;
        private String title;
        private String content;
        private LocalDate date;
        private List<String> tags; // 태그 리스트
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class DiaryListItemDto {
        private Integer id;
        private String title;
        private LocalDate date;

        public static DiaryListItemDto fromEntity(Diary diary) {
            return DiaryListItemDto.builder()
                    .id(diary.getId())
                    .title(diary.getTitle())
                    .date(diary.getDate())
                    .build();
        }
    }
}