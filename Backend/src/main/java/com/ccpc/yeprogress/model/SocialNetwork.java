package com.ccpc.yeprogress.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SocialNetwork")
@Getter
@Setter
public class SocialNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String socialNetworkURL;
}