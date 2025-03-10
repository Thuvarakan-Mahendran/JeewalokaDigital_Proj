package com.jeewaloka.digital.jeewalokadigital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GRRNDTO {

    private Long grrnid;
    private String supplierName;
    private LocalDate returneDate;
    private List<GRRNItemDTO> grrnItemList; // Changed to GRRNItemDTO
}
