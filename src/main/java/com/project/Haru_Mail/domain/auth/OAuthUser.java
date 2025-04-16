package com.project.Haru_Mail.domain.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OAuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 인증된 사용자 고유 ID

    @Column(nullable = false)
    private String googleId; // 구글에서 제공하는 사용자 고유 ID

    @Column(nullable = false)
    private String accessToken; // Google OAuth에서 받은 access token

    @Column(nullable = false)
    private String refreshToken; // Google OAuth에서 받은 refresh token
}
