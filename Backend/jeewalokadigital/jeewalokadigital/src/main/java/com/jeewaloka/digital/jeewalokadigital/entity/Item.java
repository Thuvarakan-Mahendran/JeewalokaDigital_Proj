package com.jeewaloka.digital.jeewalokadigital.entity;

import com.jeewaloka.digital.jeewalokadigital.entity.bill.BillItem;
import jakarta.persistence.*;

import java.util.List;

@Entity

@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ItemCode")
    private Long itemCode;

//    @ManyToOne
//    @JoinColumn(name = "UserID", nullable = false)
//    private User user;

    @ManyToOne
    @JoinColumn(name = "SupplierID",nullable = false)
    private Supplier supplier;

    @Column(name = "ItemName")
    private String itemName;

    @Column(name = "Type")
    private String itemType;


    @Column(name = "PurchasePrice")
    private Double itemPurchasePrice;

    @Column(name = "SalesPrice")
    private Double itemSalesPrice;

    @Column(name = "PCCode")
    private String itemPcCode;


    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<BillItem> billItems;


}
