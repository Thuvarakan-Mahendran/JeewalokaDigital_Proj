package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "price")
public class ItemPrices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PriceCode")
    private Long PcCode;

    @Column(name = "PurchasePrice")
    private Double purchasePrice;

    @Column(name = "SalesPrice")
    private Double salesPrice;

    @ManyToOne
    @JoinColumn(name = "ItemCode", referencedColumnName = "ItemCode")
    private Item item;
}
