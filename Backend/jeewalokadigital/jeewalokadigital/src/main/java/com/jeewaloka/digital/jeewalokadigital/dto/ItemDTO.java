package com.jeewaloka.digital.jeewalokadigital.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long itemCode;
    private String itemName;
    private String itemType;
    private double itemPurchasePrice;
    private double itemSalesPrice;
    private Long supplierId;

}
