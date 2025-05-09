package com.project.Haru_Mail.domain.diary;

import com.project.Haru_Mail.api.Tag.TagDto;
import com.project.Haru_Mail.api.diary.DiaryDto;
import com.project.Haru_Mail.api.diary.DiaryDto.DiaryListItemDto;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserRepository;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    private final DiaryTagRepository diaryTagRepository;

    public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository, TagRepository tagRepository, DiaryTagRepository diaryTagRepository) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.diaryTagRepository = diaryTagRepository;
    }

    // 일기+태그 저장
    public Diary createDiary(DiaryDto.DiaryRequestDto request, User user) {
        // 다이어리 객체 생성
        Diary diary = new Diary(null, request.getDiary().getTitle(), request.getDiary().getContent(), LocalDate.now(), LocalTime.now(), user);
        diary = diaryRepository.save(diary); // DB에 저장

        // 태그 처리
        for (TagDto.DiaryTagDto tagDto : request.getTags()) {
            Tag tag = tagRepository.findById(tagDto.getTagId()).get();

            // 일기와 태그 연결 (DiaryTag 저장)
            DiaryTag diaryTag = new DiaryTag();
            diaryTag.setDiary(diary);
            diaryTag.setTag(tag);
            diaryTagRepository.save(diaryTag);
        }

        return diary;
    }

    public DiaryDto.DiaryInfoDto getDiaryInfo(Integer diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("Diary not found"));

        // DiaryTag에서 tag 이름 추출
        List<DiaryTag> diaryTags = diaryTagRepository.findByDiaryId(diaryId);
        List<String> tagNames = diaryTags.stream()
                .map(diaryTag -> diaryTag.getTag().getName())
                .toList();

        return new DiaryDto.DiaryInfoDto(
                diary.getId(),
                diary.getTitle(),
                diary.getContent(),
                diary.getDate(),
                tagNames
        );
    }

    public List<DiaryListItemDto> getUserDiaries(User currentUser, int year, int month) {
        return diaryRepository.findByUser(currentUser).stream()
                .filter(diary -> diary.getDate().getYear() == year && diary.getDate().getMonthValue() == month)
                .map(DiaryListItemDto::fromEntity)
                .collect(Collectors.toList());
    }
}