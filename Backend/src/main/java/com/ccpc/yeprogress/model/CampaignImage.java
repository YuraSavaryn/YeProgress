package com.ccpc.yeprogress.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "CampaignImages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignImageId;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(columnDefinition = "TEXT")
    private String imgUrl;
}