package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import com.jeewaloka.digital.jeewalokadigital.enums.UserRole;
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
    private UserRole role;
    private Long user;
}
