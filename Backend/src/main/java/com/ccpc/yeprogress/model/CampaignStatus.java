package com.ccpc.yeprogress.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CampaignStatus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignStatusId;

    private String status;
}