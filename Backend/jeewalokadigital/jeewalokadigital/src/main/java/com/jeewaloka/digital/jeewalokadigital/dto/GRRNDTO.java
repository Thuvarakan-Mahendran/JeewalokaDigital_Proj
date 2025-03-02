package com.jeewaloka.digital.jeewalokadigital.dto;

import com.jeewaloka.digital.jeewalokadigital.entity.GRRNItem;
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
    private List<GRRNItem> grrnItemList;
}
