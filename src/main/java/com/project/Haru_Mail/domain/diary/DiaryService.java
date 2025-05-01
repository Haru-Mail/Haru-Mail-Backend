package com.project.Haru_Mail.domain.diary;

import com.project.Haru_Mail.api.diary.DiaryDto;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    //private final DiaryTagRepository diaryTagRepository; //태그랑 같이 저장할 때 사용

    public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    // 일기 저장-태그 저장 안 함
    public Diary createDiary(DiaryDto.NewDiaryDto request) {
        // 사용자 정보 조회 (userId로)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 다이어리 객체 생성
        Diary diary = Diary.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .date(LocalDate.now())
                .time(LocalTime.now())
                .user(user)
                .build();
        diary = diaryRepository.save(diary); // DB에 저장

        // 데이터베이스에 저장
        return diary;
    }
}