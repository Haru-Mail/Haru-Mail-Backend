package com.project.Haru_Mail.api.user;

import lombok.Data;

public class UserDto {
    @Data
    public static class UserSettingRequest {
        private String subscriptionFrequency;
        private boolean subscriptionAgreement;
    }
}
