package com.project.Haru_Mail.api.diary;

import lombok.AllArgsConstructor;
import lombok.Data;

public class DiaryDto {
    @Data
    @AllArgsConstructor
    public static class NewDiaryDto {
        private Integer id;
        private String title; // 제목
        private String content; // Editor.js로 작성된 JSON
        private Long userId; // 사용자 ID
    }
}