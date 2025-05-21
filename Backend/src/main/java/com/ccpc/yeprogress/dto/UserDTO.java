package com.ccpc.yeprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String firebaseId;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String location;
    private String description;
    private String imgUrl;
    private LocalDateTime createdAt;
    private Boolean isVerified;
}
