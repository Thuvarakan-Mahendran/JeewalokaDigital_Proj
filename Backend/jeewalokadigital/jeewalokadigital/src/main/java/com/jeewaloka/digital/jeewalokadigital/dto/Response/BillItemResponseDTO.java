package com.jeewaloka.digital.jeewalokadigital.dto.Response;

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
public class BillItemResponseDTO {
    private Long BIID;
    private Integer quantity;
    private Float totalValue;
    private Long bill;
    private Long item;
}
