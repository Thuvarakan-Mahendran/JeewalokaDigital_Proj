package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemDTO {

    private Long id;     // DTO can still have an ID for reference (optional)
    private Long itemId; // Stores only itemId, not an Item object
    private int quantity;
}