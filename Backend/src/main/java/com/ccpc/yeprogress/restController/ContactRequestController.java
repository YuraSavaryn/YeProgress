package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.ContactRequestDTO;
import com.ccpc.yeprogress.service.EmailRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class ContactRequestController {

    @Autowired
    private EmailRequestService emailService;

    @PostMapping("/contact")
    public ResponseEntity<Map<String, String>> sendContactMessage(@Valid @RequestBody ContactRequestDTO request) {
        try {
            emailService.sendContactEmail(request);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Повідомлення успішно відправлено!");
            response.put("status", "success");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Помилка відправки повідомлення: " + e.getMessage());
            response.put("status", "error");

            return ResponseEntity.badRequest().body(response);
        }
    }
}