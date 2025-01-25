package com.jeewaloka.digital.jeewalokadigital.dto;

import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.BillItem;
import com.jeewaloka.digital.jeewalokadigital.enums.BillCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private Long BillNO;
    private User user;
    private Retailer retailer;
    private Float total;
    private LocalDateTime date;
    private BillCategory billCategory;
    private List<BillItem> billItems;
}
