package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.ContactRequestDTO;
import com.ccpc.yeprogress.exception.UserValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmailRequestService {

    private static final Logger logger = LoggerFactory.getLogger(EmailRequestService.class);

    private final JavaMailSender mailSender;
    private final Validator validator;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${contact.email:your-contact@example.com}")
    private String contactEmail;

    @Autowired
    public EmailRequestService(JavaMailSender mailSender, Validator validator) {
        this.mailSender = mailSender;
        this.validator = validator;
    }

    public void sendContactEmail(ContactRequestDTO request) {
        logger.info("Attempting to send contact email from: {} with subject: {}",
                request.getEmail(), request.getSubject());

        try {
            validateContactRequestDTO(request);

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
            logger.info("Successfully sent contact email with subject: {}", request.getSubject());

        } catch (UserValidationException e) {
            logger.error("Validation failed for contact email: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error sending contact email: {}", e.getMessage(), e);
            throw new RuntimeException("Помилка відправки email: " + e.getMessage(), e);
        }
    }

    private void validateContactRequestDTO(ContactRequestDTO request) {
        logger.debug("Validating contact request DTO");

        Set<ConstraintViolation<ContactRequestDTO>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            logger.warn("Validation failed: {}", errorMessage);
            throw new UserValidationException("Validation failed: " + errorMessage);
        }

        if (request.getSubject().length() > 100) {
            logger.warn("Validation failed: Subject exceeds 100 characters");
            throw new UserValidationException("Subject must not exceed 100 characters");
        }
    }
}