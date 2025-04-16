package com.project.Haru_Mail.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;  // 사용자 고유 ID

    @Column(nullable = false, unique = true)
    private String googleId;  // 구글에서 제공하는 고유 사용자 ID (Google OAuth로 인증된 사용자만 해당)

    @Column(nullable = false, unique = true)
    private String email;  // 구글 이메일 (사용자의 이메일 주소)

    private String username;  // 사용자 이름 (구글 프로필 또는 사용자가 설정)

    private Integer frequency;  // 사용자가 얼마나 자주 메일을 받을지 설정하는 값 (메일 발송 주기)
}
