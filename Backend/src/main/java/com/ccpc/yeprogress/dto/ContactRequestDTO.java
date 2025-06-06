package com.ccpc.yeprogress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDTO {
    @NotBlank(message = "Ім'я не може бути порожнім")
    @Size(max = 50)
    private String name;

    @Email(message = "Невалідна email адреса")
    @NotBlank(message = "Email обов'язковий")
    private String email;

    @NotBlank(message = "Оберіть тему повідомлення")
    private String subject;

    @NotBlank(message = "Поле повідомлення обов'язкове")
    @Size(max = 1000)
    private String message;

}