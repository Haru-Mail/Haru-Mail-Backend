package com.project.Haru_Mail.domain.question;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    Optional<Question> findByContent(String questionContent);
}
