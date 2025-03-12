package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsRequestDTO {
    private String username;
    private String password;
    private Long usermark;
}
