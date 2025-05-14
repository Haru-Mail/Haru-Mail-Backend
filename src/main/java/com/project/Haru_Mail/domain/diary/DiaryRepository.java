package com.project.Haru_Mail.domain.diary;

import com.project.Haru_Mail.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findByUser(User user);
    List<Diary> findByIdInAndUserUserId(List<Integer> ids, Long userId);
}
