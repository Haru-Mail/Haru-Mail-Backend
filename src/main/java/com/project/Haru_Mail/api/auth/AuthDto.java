package com.project.Haru_Mail.api.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AuthDto {

    // 로그인 시 반환되는 응답 DTO
    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private String googleId;   // Google OAuth ID (로그인 시만 필요)
        private String email;
        private String username;
    }

    // 사용자 정보 조회 응답 DTO (주로 '/auth/me' 요청 시 사용)
    @Data
    @AllArgsConstructor
    public static class UserInfo {
        private String email;
        private String username;
    }
}
