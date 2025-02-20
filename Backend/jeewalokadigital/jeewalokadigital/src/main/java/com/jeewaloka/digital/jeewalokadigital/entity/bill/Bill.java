package com.jeewaloka.digital.jeewalokadigital.entity.bill;

import com.jeewaloka.digital.jeewalokadigital.entity.Retailer;
import com.jeewaloka.digital.jeewalokadigital.entity.User;
import com.jeewaloka.digital.jeewalokadigital.enums.BillCategory;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSeq")
    @SequenceGenerator(name = "idSeq", sequenceName = "idSeq", allocationSize = 1)
    private Long BillNO;
    @ManyToOne
    @JoinColumn(name = "UID", referencedColumnName = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "RID", referencedColumnName = "RetailerId")
    private Retailer retailer;
    private Float total;
    private LocalDate date; // date bill record was created
    private BillCategory billCategory;
    @OneToMany(mappedBy = "bill",cascade = CascadeType.ALL)
    private List<BillItem> billItems;

    public Bill() {
    }

    public Long getBillNO() {
        return BillNO;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BillCategory getBillCategory() {
        return billCategory;
    }

    public void setBillCategory(BillCategory billCategory) {
        this.billCategory = billCategory;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }
}
