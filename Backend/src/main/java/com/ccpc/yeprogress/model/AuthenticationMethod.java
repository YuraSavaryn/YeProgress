package com.ccpc.yeprogress.model;

import com.ccpc.yeprogress.model.types.CampaignStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AuthenticationMethod")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authMethodId;

    private String authMethodName;
}
