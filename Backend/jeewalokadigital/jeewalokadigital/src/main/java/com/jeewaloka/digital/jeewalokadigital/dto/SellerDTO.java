package com.jeewaloka.digital.jeewalokadigital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SellerDTO {

    private int sellerId;
    private String sellerName;
    private String sellerTelephone;
    private String sellerEmail;
    private String sellerAddress;
}
