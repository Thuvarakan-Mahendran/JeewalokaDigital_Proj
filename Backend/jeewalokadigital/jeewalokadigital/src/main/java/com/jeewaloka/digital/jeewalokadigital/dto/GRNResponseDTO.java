package com.jeewaloka.digital.jeewalokadigital.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GRNResponseDTO {
    // Getters and Setters
    private Long grnId;
    private String grnCode;
    private String grnReceivedBy;
    private Double grnTotalAmount;
    private String grnStatus;
    private String grnSupplierName;
    private List<GRNItemResponseDTO> grnItems;

}
