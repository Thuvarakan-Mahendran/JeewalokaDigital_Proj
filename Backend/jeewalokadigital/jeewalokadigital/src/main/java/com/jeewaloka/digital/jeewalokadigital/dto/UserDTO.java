package com.jeewaloka.digital.jeewalokadigital.dto;

import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String uname;
    private String contact;
    private String email;
    private UserRole role;
    private LocalDateTime lastLogin;
    private List<Bill> bills;
}
