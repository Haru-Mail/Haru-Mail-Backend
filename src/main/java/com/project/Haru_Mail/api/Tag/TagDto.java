package com.project.Haru_Mail.api.Tag;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


public class TagDto {
    @Data
    @AllArgsConstructor
    public static class TagRequestDto { //태그 생성
        private String name;
        private Integer categoryId;
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    public static class ShowTagDto { //태그 조회
        private String name;
        private Integer categoryId;
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    public static class DiaryTagDto { //일기 태그 저장
        private Integer tagId;
    }
}
