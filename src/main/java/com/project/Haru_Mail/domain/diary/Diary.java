package com.project.Haru_Mail.domain.diary;

import com.project.Haru_Mail.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Diary")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 다이어리 ID

    @Column(columnDefinition = "NVARCHAR(255)")
    private String title; // 제목

    @Lob
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String content; // 내용

    private LocalDate date; // 작성 날짜

    private LocalTime time; // 작성 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 사용자 ID
}
