package com.ccpc.yeprogress.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationMethodDTO {
    private Long authMethodId;
    private String authMethodName;

}
