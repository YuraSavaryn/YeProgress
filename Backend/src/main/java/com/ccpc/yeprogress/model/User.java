package com.ccpc.yeprogress.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "firebase_id", unique = true, nullable = false)
    private String firebaseId;

    private String name;

    private String surname;

    private String phone;

    private String email;

    private String location;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_verified")
    private Boolean isVerified;
}
