package com.project.Haru_Mail.domain.refreshToken;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14) // 14일
public class RefreshToken {

    @Id
    private String id;  // 고유 식별자 필드를 추가

    private String email; // 이메일은 id로 사용하지 않음

    private String refreshToken;

    @Indexed
    private String accessToken;

    public RefreshToken(String email, String refreshToken, String accessToken) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
