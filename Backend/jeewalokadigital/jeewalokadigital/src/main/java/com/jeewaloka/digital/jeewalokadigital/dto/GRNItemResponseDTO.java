package com.jeewaloka.digital.jeewalokadigital.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class GRNItemResponseDTO {
    // Getters and Setters
    private Long itemId;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
    private LocalDate itemExpiryDate;
    private LocalDate itemManufactureDate;

}
