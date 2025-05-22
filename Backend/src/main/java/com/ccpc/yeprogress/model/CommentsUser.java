package com.ccpc.yeprogress.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CommentsUser")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
}