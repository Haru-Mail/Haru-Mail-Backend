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
@RedisHash("refreshToken")
public class RefreshToken implements Serializable {

    @Id
    private String refreshToken;  // Redis key

    private String email;         // 누구의 토큰인지 식별
}