package com.jeewaloka.digital.jeewalokadigital.dto.Request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RequestRetailerDTO {
    private Integer RetailerId;
    private String RetailerName;
    private String RetailerContactNo;
    private String RetailerAddress;
    private String RetailerEmail;
}
