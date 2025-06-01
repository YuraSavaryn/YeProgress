package com.ccpc.yeprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDTO {
    private Long authenticationId;
    private String methodName;
    private String statusName;
    private String externalAuthId;
    private LocalDateTime verifiedAt;
}
