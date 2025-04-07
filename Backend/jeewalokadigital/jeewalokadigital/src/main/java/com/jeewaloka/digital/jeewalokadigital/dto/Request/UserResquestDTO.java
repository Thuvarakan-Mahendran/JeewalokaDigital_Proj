package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import com.jeewaloka.digital.jeewalokadigital.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResquestDTO {
    private String uname;
    private String contact;
    private String email;
//    private UserRole role;
//    private LocalDate lastLogin;
//    private List<Long> billIDS;
}
