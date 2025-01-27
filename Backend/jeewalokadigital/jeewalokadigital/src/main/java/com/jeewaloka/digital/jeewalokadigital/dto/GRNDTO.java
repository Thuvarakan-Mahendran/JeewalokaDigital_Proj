package com.jeewaloka.digital.jeewalokadigital.dto;

import com.jeewaloka.digital.jeewalokadigital.entity.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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


