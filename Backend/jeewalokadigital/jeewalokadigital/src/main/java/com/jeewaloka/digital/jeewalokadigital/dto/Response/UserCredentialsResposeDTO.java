package com.jeewaloka.digital.jeewalokadigital.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsResposeDTO {
    private Long UserCredID;
    private String username;
    private String role;
    private Long user;
}
