package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ItemCode")
    private Long itemCode;

//    @ManyToOne
//    @JoinColumn(name = "UserID", nullable = false)
//    private User user;

    @ManyToOne
    @JoinColumn(name = "SupplierID", nullable = false)
    private Supplier supplier;

    @Column(name = "ItemName", nullable = false)
    private String itemName;

    @Column(name = "Type")
    private String itemType;


    @Column(name = "PurchasePrice")
    private Double itemPurchasePrice;

    @Column(name = "SalesPrice")
    private Double itemSalesPrice;

    @Column(name = "PCCode")
    private String itemPcCode;

}
