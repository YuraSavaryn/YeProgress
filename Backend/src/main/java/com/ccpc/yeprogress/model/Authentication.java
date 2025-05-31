package com.ccpc.yeprogress.model;

import com.ccpc.yeprogress.model.types.AuthenticationStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Authentications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authenticationId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "method_id")
    private AuthenticationMethod method;

    @Enumerated(EnumType.STRING)
    private AuthenticationStatusType status;

    private String externalAuthId;
    private LocalDateTime verifiedAt;
}