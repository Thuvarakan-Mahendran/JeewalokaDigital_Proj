package com.jeewaloka.digital.jeewalokadigital.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GRNItemDTO {
    private Long grnItemId;
    private Long itemId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
    private LocalDate itemExpiryDate;
    private LocalDate itemManufactureDate;


}
