package com.jeewaloka.digital.jeewalokadigital.dto.Request;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class RequestRetailerDTO {
    private String RetailerId;
    private String RetailerName;
    private String RetailerContactNo;
    private String RetailerAddress;
    private String RetailerEmail;
    @Setter
    private List<Long> billIds;

}
