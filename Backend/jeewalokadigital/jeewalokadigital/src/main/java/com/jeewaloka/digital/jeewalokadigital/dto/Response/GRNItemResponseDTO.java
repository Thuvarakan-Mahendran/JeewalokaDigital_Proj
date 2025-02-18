package com.jeewaloka.digital.jeewalokadigital.dto.Response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class GRNItemResponseDTO {
    private Long itemId;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private Double unitPrice;
    private LocalDate itemExpiryDate;
    private LocalDate itemManufactureDate;
}
