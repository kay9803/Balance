// src/main/java/com/judebalance/backend/service/EmailService.java
package com.judebalance.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 이메일 발송을 담당하는 서비스
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String toEmail, String token) {
        String resetLink = "https://your-app.com/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[JudeBalance] 비밀번호 재설정 링크 안내");
        message.setText("비밀번호를 재설정하려면 아래 링크를 클릭하세요:\n\n" + resetLink);

        mailSender.send(message);
    }
}
