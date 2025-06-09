package com.project.Haru_Mail.domain.mailing;

import com.project.Haru_Mail.api.mailing.MailRequestDto;
import com.project.Haru_Mail.domain.question.Question;
import com.project.Haru_Mail.domain.question.QuestionRepository;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@AllArgsConstructor
public class MailingService {
    private final JavaMailSender mailSender;
    private final MailLogRepository mailingRepository;
    private final UserRepository userRepository;
    private final TemplateEngine templateEngine;
    private final QuestionRepository questionRepository;

    @Scheduled(cron = "0 0 20 * * *", zone =  "Asia/Seoul")
    public void scheduledMailingJob(){
        try{
            log.info("메일 발송 작업 시작");
            sendDailyMailToAllUsers();
            log.info("메일 발송 완료");
        }catch (Exception e){
            log.error("메일 발송 중 오류 발생", e);
        }
    }

    // 임시 질문
    private final String[] allQuestions = {
            "오늘 가장 인상 깊었던 순간은 무엇이었나요?",
            "오늘 어떤 감정을 느꼈나요? 가장 강렬했던 감정은 무엇이었나요?",
            "오늘 하루를 세 단어로 표현한다면?",
            "오늘 나를 가장 행복하게 했던 작은 일은 무엇이었나요?",
            "오늘 나를 힘들게 했던 것은 무엇이었나요? 어떻게 극복했나요?",
            "오늘 문득 떠올랐던 생각은 무엇이었나요?",
            "오늘 가장 많이 했던 생각은 무엇이었나요?",
            "오늘의 나에게 칭찬해주고 싶은 점이 있다면?",
            "오늘의 나에게 아쉬웠던 점이 있다면? 내일은 어떻게 개선할 수 있을까요?",
            "오늘 가장 기억에 남는 꿈을 꾸었나요?",
            "오늘 감사했던 일 세 가지를 적어볼까요?",
            "오늘 당신에게 친절을 베풀어준 사람은 누구였나요?",
            "오늘 당신이 다른 사람에게 베풀었던 친절은 무엇이었나요?",
            "오늘 당신의 마음을 따뜻하게 해준 것은 무엇이었나요?",
            "오늘 당연하게 여겼지만, 사실 감사해야 할 것은 무엇이었나요?",
            "오늘 가장 웃겼던 순간은 언제였나요?",
            "오늘 당신을 미소짓게 만든 것은 무엇이었나요?",
            "오늘 당신에게 활력을 불어넣어 준 것은 무엇이었나요?",
            "오늘 당신이 다른 사람을 웃게 만들었던 순간이 있었나요?",
            "오늘 당신이 발견한 재미있는 것은 무엇이었나요?",
            "오늘 새롭게 알게 된 사실이 있나요?",
            "오늘 당신이 배운 가장 중요한 것은 무엇이었나요?",
            "오늘 당신의 생각을 확장시킨 것은 무엇이었나요?",
            "오늘 당신이 어떤 도전에 직면했고, 어떻게 해결했나요?",
            "오늘 당신이 성장했다고 느낀 부분은 어디인가요?",
            "오늘 당신에게 힘이 되어준 사람은 누구였나요?",
            "오늘 당신이 소중하게 느꼈던 관계는 무엇이었나요?",
            "오늘 당신이 연락했던 사람 중 가장 기억에 남는 사람은 누구인가요?",
            "오늘 당신이 다른 사람에게 어떤 도움을 주었나요?",
            "오늘 당신이 받고 싶었던 따뜻한 말이나 행동은 무엇이었나요?",
            "오늘 당신이 계획했던 일 중 가장 잘 마무리된 것은 무엇인가요?",
            "오늘 당신의 목표 달성에 한 걸음 더 다가갔나요? 어떤 부분에서 그렇게 느꼈나요?",
            "내일 당신이 가장 기대하는 것은 무엇인가요?",
            "오늘 당신이 시간을 가장 의미있게 사용했던 때는 언제인가요?",
            "오늘 당신의 에너지를 가장 많이 쏟았던 일은 무엇인가요?",
            "오늘 당신의 기분 상태를 색깔로 표현한다면 어떤 색깔일까요?",
            "오늘 당신의 하루를 동물에 비유한다면 어떤 동물일까요?",
            "오늘 당신이 가장 중요하게 생각했던 가치는 무엇이었나요?",
            "오늘 당신의 행동은 당신의 가치관과 일치했나요?",
            "만약 오늘을 다시 살 수 있다면, 무엇을 다르게 하고 싶나요?",
            "오늘 당신이 먹었던 음식 중 가장 맛있었던 것은 무엇이었나요?",
            "오늘 당신이 들었던 음악 중 가장 좋았던 곡은 무엇인가요?",
            "오늘 당신이 보았던 풍경 중 가장 아름다웠던 것은 무엇이었나요?",
            "오늘 당신의 몸과 마음을 위해 어떤 노력을 기울였나요?",
            "오늘 당신에게 영감을 준 것은 무엇이었나요?",
            "오늘 당신을 설레게 한 것은 무엇이었나요?",
            "오늘 당신이 가장 편안함을 느꼈던 순간은 언제였나요?",
            "오늘 당신이 가장 창의적인 아이디어를 떠올렸던 순간은 언제인가요?",
            "오늘 당신에게 가장 큰 영향을 준 뉴스는 무엇이었나요?",
            "오늘 하루, 당신은 자신에게 어떤 이야기를 해주고 싶나요?"
    };

    public void sendDailyMailToAllUsers() throws Exception{
        List<User> users = userRepository.findAll();

        // 관리자 이메일 주소 (이 주소는 메일 발송 대상에서 제외)
        String adminEmail = "hahahalumail@gmail.com";

        LocalDate today = LocalDate.now();
        String dayOfWeek = convertDayOfWeek(today.getDayOfWeek());

        for (User user: users){
            // 관리자 이메일이면 건너뜀
            if (adminEmail.equalsIgnoreCase(user.getEmail())) continue;

            if (!shouldSendToday(user, today)) continue;

            int questionIndex = user.getQ_index();
            String questionContent = allQuestions[questionIndex % allQuestions.length]; // 순환 로직

            Question todayQuestion = questionRepository.findByContent(questionContent)
                    .orElseGet(() -> {
                        Question q = new Question();
                        q.setContent(questionContent);
                        return questionRepository.save(q);
                    });

            MailRequestDto dto = new MailRequestDto(
                    user.getEmail(),
                    user.getUsername(),
                    todayQuestion.getContent(),
                    today.getYear(),
                    today.getMonthValue(),
                    today.getDayOfMonth(),
                    dayOfWeek
            );
            sendMail(dto, user, todayQuestion);

            // 다음 질문 인덱스로 업데이트
            user.setQ_index((questionIndex+1) % allQuestions.length);
            userRepository.save(user);
        }
    }

    private boolean shouldSendToday(User user, LocalDate today) {
        int freq = user.getFrequency();
        // 7: 매일, 3: 격일 (월, 수, 금), 1: 주간 (일요일)
        if (freq == 7) return true; // 매일
        if (freq == 3) { // 격일 (월, 수, 금)
            DayOfWeek currentDay = today.getDayOfWeek();
            return currentDay == DayOfWeek.MONDAY || currentDay == DayOfWeek.WEDNESDAY || currentDay == DayOfWeek.FRIDAY;
        }
        if (freq == 1) return today.getDayOfWeek() == DayOfWeek.SUNDAY; // 주간 (일요일)
        return false; // 그 외 (빈도 0 또는 알 수 없는 값)
    }

    private void sendMail(MailRequestDto dto, User user, Question todayQuestion) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(new InternetAddress(user.getEmail(), "하루 메일", "UTF-8"));

        // 템플릿에 변수 전달
        Context context = new Context();
        context.setVariable("year", dto.getYear());
        context.setVariable("month", dto.getMonth());
        context.setVariable("day", dto.getDay());
        context.setVariable("dayOfWeek", dto.getDayOfWeek());
        context.setVariable("questionText", dto.getQuestionText());

        String html = templateEngine.process("mail/question-mail.html", context);
        helper.setTo(dto.getEmail());
        helper.setSubject("하루 메일이 도착했어요 ✉");
        helper.setText(html, true);

        // 이미지 첨부
        ClassPathResource imageResource = new ClassPathResource("static/images/MailImage.png");
        helper.addInline("MailImage", imageResource);

        mailSender.send(message);


        // 로그 저장
        MailLog mailLog = new MailLog();
        mailLog.setUser(user);
        mailLog.setQuestion(todayQuestion);
        mailLog.setSentDate(LocalDateTime.now());
        mailingRepository.save(mailLog);
    }

    private String convertDayOfWeek(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
    }

    public void settingMailingFrequency(User currentUser, Map<String, String> body){
        String frequency = body.get("selectedOption");

        if("daily".equalsIgnoreCase(frequency)){
            currentUser.setFrequency(7);
        }else if("every_other_day".equalsIgnoreCase(frequency)){
            currentUser.setFrequency(3);
        }else if("weekly".equalsIgnoreCase(frequency)){
            currentUser.setFrequency(1);
        }else{
            currentUser.setFrequency(0);
        }
        userRepository.save(currentUser);
    }
}
