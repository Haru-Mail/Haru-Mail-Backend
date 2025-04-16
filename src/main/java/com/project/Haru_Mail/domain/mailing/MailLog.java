package com.project.Haru_Mail.domain.mailing;

import com.project.Haru_Mail.domain.question.Question;
import com.project.Haru_Mail.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 메일 로그 고유 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 (어떤 사용자가 질문을 받았는지)

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question; // 질문 (어떤 질문을 받았는지)

    @Column(nullable = false)
    private LocalDateTime sentDate; // 메일 전송 날짜 및 시간
}
