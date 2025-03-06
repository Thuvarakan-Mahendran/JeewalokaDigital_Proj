package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
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


    public void setGrn(GRRN grrn) {

    }
}
