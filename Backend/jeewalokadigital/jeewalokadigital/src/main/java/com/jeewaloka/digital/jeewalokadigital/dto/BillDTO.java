package com.jeewaloka.digital.jeewalokadigital.dto;

import com.jeewaloka.digital.jeewalokadigital.entity.bill.BillItem;
import com.jeewaloka.digital.jeewalokadigital.enums.BillCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private Long BillNO;
    private Long UID;
    private Long RID;
    private Float total;
    private LocalDateTime date;
    private BillCategory billCategory;
    private List<BillItem> billItems;
}
