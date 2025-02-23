package com.jeewaloka.digital.jeewalokadigital.dto.Request;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestRetailerDTO {
    private Integer RetailerId;
    private String RetailerName;
    private int RetailerContactNo;
    private String RetailerAddress;
    private String RetailerEmail;
    private List<Long> billIds;
}
