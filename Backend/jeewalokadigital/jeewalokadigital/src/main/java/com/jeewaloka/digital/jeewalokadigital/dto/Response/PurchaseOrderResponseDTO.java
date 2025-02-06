package com.jeewaloka.digital.jeewalokadigital.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderResponseDTO {

    private Long poId;
    private String  supplierName;
    private LocalDate orderDate;
    private String status = "PENDING";
    private List<PurchaseOrderItemResponseDTO> poItems;


}
