package com.ccpc.yeprogress.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CommentsCampaign")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean complaint;

    private LocalDateTime createdAt;
}