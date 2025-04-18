package com.jeewaloka.digital.jeewalokadigital.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.BillItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
//@JsonIgnoreProperties()
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ItemCode")
    private Long itemCode;

//    @Column(name = "ItemCode2")
//    private String itemCode2;

    @ManyToOne
    @JoinColumn(name = "SupplierID", nullable = false)
    private Supplier supplier;

    @Column(name = "ItemName")
    private String itemName;

    @Column(name = "Type")
    private String itemType;

    @Column(name = "PurchasePrice")
    private Double itemPurchasePrice;

    @Column(name = "SalesPrice")
    private Double itemSalesPrice;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPrices> priceItems;


    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPrices> prices;

    @Column(name = "Status")
    private String status;
 
    @Transient
    private Integer totalQuantityInStock;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<BillItem> billItems;

}
