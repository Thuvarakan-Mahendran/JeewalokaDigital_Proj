package com.jeewaloka.digital.jeewalokadigital.dto.Response;

import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PurchaseOrderItemResponseDTO {

    private Long itemCode;
    private String itemName;
    private int quantity;

}
