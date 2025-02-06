package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDTO {

    private Long id;


    private Long supplierId; // Renamed to camelCase

    private LocalDate orderDate;

    private String status ;

    private String remarks;

    private List<PurchaseOrderItemDTO> pItems = new ArrayList<>(); // Use PurchaseOrderItemDTO for item list
}
