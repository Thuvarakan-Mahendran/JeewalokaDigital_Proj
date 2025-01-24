package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;

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

    public Long getItemCode() {
        return itemCode;
    }

    public void setItemCode(Long itemCode) {
        this.itemCode = itemCode;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Double getItemPurchasePrice() {
        return itemPurchasePrice;
    }

    public void setItemPurchasePrice(Double itemPurchasePrice) {
        this.itemPurchasePrice = itemPurchasePrice;
    }

    public Double getItemSalesPrice() {
        return itemSalesPrice;
    }

    public void setItemSalesPrice(Double itemSalesPrice) {
        this.itemSalesPrice = itemSalesPrice;
    }

    public String getItemPcCode() {
        return itemPcCode;
    }

    public void setItemPcCode(String itemPcCode) {
        this.itemPcCode = itemPcCode;
    }
}
