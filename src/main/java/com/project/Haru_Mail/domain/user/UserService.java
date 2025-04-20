package com.project.Haru_Mail.domain.user;

import com.project.Haru_Mail.common.jwt.JwtTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private JwtTokenizer jwtTokenizer;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 이메일로 사용자 찾기
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 사용자 저장
    public void saveUser(User user) {
        userRepository.save(user);
    }
}