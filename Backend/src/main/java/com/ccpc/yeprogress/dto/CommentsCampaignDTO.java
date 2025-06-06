package com.ccpc.yeprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsCampaignDTO {
    private Long commentId;
    private Long userId;
    private Long campaignId;
    private String content;
    private LocalDateTime createdAt;
}