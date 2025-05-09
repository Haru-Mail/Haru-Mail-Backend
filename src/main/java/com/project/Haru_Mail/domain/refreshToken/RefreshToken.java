package com.project.Haru_Mail.domain.refreshToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("refreshToken")  // Redis key prefix
public class RefreshToken implements Serializable {

    @Id
    private String refreshToken; // 이 값이 Redis의 key로 사용됨

    private String accessToken;

    private String email;
}
