package com.jeewaloka.digital.jeewalokadigital.entity.bill;

import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BillItem {
    private Long BIID;
    private Integer quantity;
    private Float totalValue;
    @ManyToOne
    @JoinColumn(name = "BillNO",referencedColumnName = "BillNO")
    private Bill bill;
    @ManyToOne
    @JoinColumn(name = "itemCode",referencedColumnName = "itemCode")
    private Item item;
}
