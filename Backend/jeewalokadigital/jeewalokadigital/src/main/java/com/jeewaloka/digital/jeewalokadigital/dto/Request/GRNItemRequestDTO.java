package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GRNItemRequestDTO {
    private Long grnItemId;
    private Long itemId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
    private LocalDate itemExpiryDate;
    private LocalDate itemManufactureDate;


}
