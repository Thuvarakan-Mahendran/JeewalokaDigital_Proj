package com.jeewaloka.digital.jeewalokadigital.entity.bill;

import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import jakarta.persistence.*;

@Entity
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSeq")
    @SequenceGenerator(name = "idSeq", sequenceName = "idSeq", allocationSize = 1)
    private Long BIID;
    private Integer quantity;
    private Float totalValue;
    @ManyToOne
    @JoinColumn(name = "BillNO",referencedColumnName = "BillNO")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "itemCode",referencedColumnName = "itemCode")
    private Item item;

    public BillItem() {
    }

    public Long getBIID() {
        return BIID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Float totalValue) {
        this.totalValue = totalValue;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
