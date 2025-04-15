package com.jeewaloka.digital.jeewalokadigital.dto.Response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseRetailerDTO {
    private String RetailerId;
    private String RetailerName;
    private String RetailerContactNo;
    private String RetailerAddress;
    private String retailerEmail;
    private Float LimitCredit;
    private List<Long> billIds;
}