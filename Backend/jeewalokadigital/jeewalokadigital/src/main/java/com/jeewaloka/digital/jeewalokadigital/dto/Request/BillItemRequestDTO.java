package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillItemRequestDTO {
    private Integer quantity;
    private Float totalValue;
//    private Long bill;
    private Long item;
}
