package com.ccpc.yeprogress.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.ccpc.yeprogress.dto.ContactRequestDTO;


@Service
public class EmailRequestService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${contact.email:your-contact@example.com}")
    private String contactEmail;

    public void sendContactEmail(ContactRequestDTO request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(contactEmail);
            message.setSubject("Нове повідомлення: " + request.getSubject());

            String body = String.format(
                    "Нове повідомлення від:\n\n" +
                            "Ім'я: %s\n" +
                            "Email: %s\n" +
                            "Тема: %s\n\n" +
                            "Повідомлення:\n%s",
                    request.getName(),
                    request.getEmail(),
                    request.getSubject(),
                    request.getMessage()
            );

            message.setText(body);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Помилка відправки email: " + e.getMessage());
        }
    }
}