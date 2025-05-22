package com.ccpc.yeprogress.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "AuthenticationStatus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authStatusId;

    private String authStatusName;
}
