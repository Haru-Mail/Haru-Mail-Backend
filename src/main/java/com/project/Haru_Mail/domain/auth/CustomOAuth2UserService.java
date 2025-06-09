package com.project.Haru_Mail.domain.auth;

import com.project.Haru_Mail.common.jwt.JwtTokenizer;
import com.project.Haru_Mail.domain.mailing.MailingService;
import com.project.Haru_Mail.domain.user.User;
import com.project.Haru_Mail.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepo;
    private final JwtTokenizer jwtTokenizer;
    private final MailingService mailingService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(req);

        String googleId = oAuth2User.<String>getAttribute("sub");
        String email    = oAuth2User.<String>getAttribute("email");
        String name     = oAuth2User.getAttribute("name");

        Optional<User> existingUser = userRepo.findByGoogleId(googleId);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = userRepo.save(User.builder()
                    .googleId(googleId)
                    .email(email)
                    .username(name)
                    .frequency(7)
                    .q_index(0)
                    .agreeToMail(false)
                    .build());

            try {
                mailingService.sendSubscriptionConfirmationEmail(user); // ✅ 신규 사용자에 한해 메일 발송
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        String token = jwtTokenizer.createToken(email);
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("token", token);

        return new DefaultOAuth2User(
                Collections.emptyList(),
                attributes, "email"
        );
    }
}