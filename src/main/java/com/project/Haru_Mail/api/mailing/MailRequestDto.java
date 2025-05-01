package com.project.Haru_Mail.api.mailing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailRequestDto {
    private String email;
    private String userName;
    private String questionText;
    private int year;
    private int month;
    private int day;
    private String dayOfWeek;
}
