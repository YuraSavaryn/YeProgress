package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.ContactRequestDTO;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.validation.ValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailRequestService {

    private static final Logger logger = LoggerService.getLogger(EmailRequestService.class);

    private final JavaMailSender mailSender;
    private final ValidationService validationService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${contact.email:your-contact@example.com}")
    private String contactEmail;

    @Autowired
    public EmailRequestService(JavaMailSender mailSender, ValidationService validationService) {
        this.mailSender = mailSender;
        this.validationService = validationService;
    }

    public void sendContactEmail(ContactRequestDTO request) {
        LoggerService.logEmailAttempt(logger, request.getEmail(), request.getSubject());

        try {
            validationService.validateContactRequestDTO(request);

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
            LoggerService.logEmailSuccess(logger, request.getSubject());

        } catch (UserValidationException e) {
            LoggerService.logError(logger, "Validation failed for contact email: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "contact email sending", e.getMessage(), e);
            throw new RuntimeException("Помилка відправки email: " + e.getMessage(), e);
        }
    }
}