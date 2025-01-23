package com.jeewaloka.digital.jeewalokadigital.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditBillDTO {
    private Long BillNO;
    private Long UID;
    private Long RID;
    private Long ICODE;
    private String values;
    private Integer quantity;
    private Float total;
    private LocalDateTime date;
    private Float paidAmount;
}
