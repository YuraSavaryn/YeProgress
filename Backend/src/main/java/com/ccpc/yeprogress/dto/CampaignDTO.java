package com.ccpc.yeprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDTO {
    private Long campaignId;
    private String title;
    private String description;
    private String bankaUrl;
    private BigDecimal currentAmount;
    private BigDecimal goalAmount;
    private LocalDateTime createdDate;
    private LocalDateTime approxDeadline;
    private String status;
    private String mainImgUrl;

}