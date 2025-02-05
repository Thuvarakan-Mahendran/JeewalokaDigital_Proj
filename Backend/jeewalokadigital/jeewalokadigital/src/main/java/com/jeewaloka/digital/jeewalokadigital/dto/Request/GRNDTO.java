package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GRNDTO {


    private Long grnId;
    private Long grnSupplierId;
    private String grnReceivedBy;
    private Double grnTotalAmount;
    private String grnStatus;
    private List<GRNItemDTO> grnItems;
}


