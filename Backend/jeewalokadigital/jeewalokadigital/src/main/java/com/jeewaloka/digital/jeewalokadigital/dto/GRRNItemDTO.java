package com.jeewaloka.digital.jeewalokadigital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GRRNItemDTO {

    private Long grrnitemId;
    private String itemName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
    private LocalDate itemExpiryDate;
}
