package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "seller")
public class Retailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RetailerId", nullable = false, length = 10)
    private Integer RetailerId;

    @Column(name = "RetailerName", nullable = false, length = 45 )
    private String RetailerName;

    @Column(name = "RetailerContactNo", nullable = false, length = 10 )
    private int RetailerContactNo;

    @Column(name = "RetailerAddress", nullable = false, length = 45 )
    private String RetailerAddress;

    @Column(name = "RetailerEmail", nullable = false, length = 45 )
    private String RetailerEmail;
}
