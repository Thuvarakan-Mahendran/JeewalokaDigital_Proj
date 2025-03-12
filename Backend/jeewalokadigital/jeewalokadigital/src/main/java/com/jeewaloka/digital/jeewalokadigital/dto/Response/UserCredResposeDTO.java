package com.jeewaloka.digital.jeewalokadigital.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredResposeDTO {
    private Long UserCredID;
    private String username;
    private String password;
    private Long UID;
}
