package com.ccpc.yeprogress.model;

import com.ccpc.yeprogress.model.types.CampaignStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Campaigns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;
    @Column(unique = true)
    private String bankaUrl;
    private BigDecimal currentAmount;
    private BigDecimal goalAmount;
    private LocalDateTime createdDate;
    private LocalDateTime approxDeadline;

    @Enumerated(EnumType.STRING)
    private CampaignStatusType status;

    private String mainImgUrl;
}