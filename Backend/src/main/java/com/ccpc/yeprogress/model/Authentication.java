package com.ccpc.yeprogress.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Authentications")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authenticationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "method_id")
    private AuthenticationMethod method;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private AuthenticationStatus status;

    private String externalAuthId;
    private LocalDateTime verifiedAt;
}
