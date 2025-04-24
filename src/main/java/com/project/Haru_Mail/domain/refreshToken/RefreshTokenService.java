package com.project.Haru_Mail.domain.refreshToken;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void save(String email, String refreshToken, String accessToken){
        RefreshToken token = new RefreshToken(email, refreshToken, accessToken);
        refreshTokenRepository.save(token);
    }

    public RefreshToken findByAccessToken(String accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new RuntimeException("해당 accessToken으로 저장된 RefreshToken이 없습니다."));
    }

    public void deleteByEmail(String email) {
        refreshTokenRepository.deleteById(email);
    }
}
