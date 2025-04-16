package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class GRNItemRequestDTO {
    private Long itemId;
    private Integer quantity;
    private Double unitPrice;
    private LocalDate itemExpiryDate;

}
