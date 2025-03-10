package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class GRRNItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long grrnitemId;

    @ManyToOne
    @JoinColumn(name = "grrn_id")
    private GRRN grrn;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private LocalDate itemExpiryDate;

    // Constructor with fields excluding grrn (handled manually)
    public GRRNItem(String itemName, Integer quantity, Double unitPrice, Double totalAmount, LocalDate itemExpiryDate) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.itemExpiryDate = itemExpiryDate;
    }
}
