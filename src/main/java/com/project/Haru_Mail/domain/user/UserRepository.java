package com.project.Haru_Mail.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByEmail(String email);
}