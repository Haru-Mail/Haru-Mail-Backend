package com.project.Haru_Mail.api.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AuthDto {
    @Data
    @AllArgsConstructor
    public static class LoginResponse{
        private String googleId;
        private String email;
    }

    @Data
    @AllArgsConstructor
    public static class UserInfo{
        private String googleId;
        private String email;
    }
}
