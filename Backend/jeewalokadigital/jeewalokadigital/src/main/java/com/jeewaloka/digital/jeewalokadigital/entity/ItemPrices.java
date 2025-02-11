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
    @Column(name = "PriceID")
    private Long priceId;

    @ManyToOne
    @JoinColumn(name = "ItemCode", nullable = false)
    private Item item;


    @Column(name = "PurchasePrice")
    private Double purchasePrice;

    @Column(name = "SalesPrice")
    private Double salesPrice;

    @Column(name = "EffectiveDate")
    private LocalDateTime effectiveDate;
}
