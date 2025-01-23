package com.jeewaloka.digital.jeewalokadigital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class ItemDTO {
    private Long itemCode;
    private String itemName;
    private String itemType;
    private double itemPurchasePrice;
    private int itemSalesPrice;


}


