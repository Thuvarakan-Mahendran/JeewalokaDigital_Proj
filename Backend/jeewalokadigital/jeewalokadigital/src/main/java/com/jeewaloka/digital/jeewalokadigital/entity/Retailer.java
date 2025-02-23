package com.jeewaloka.digital.jeewalokadigital.entity;

import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "retailer")
public class Retailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RetailerId", length = 10)  // is it good to define RestailerId as retailerId?
    private Integer RetailerId;

    @Column(name = "RetailerName", length = 45 )
    private String RetailerName;

    @Column(name = "RetailerContactNo",  length = 10 )
    private int RetailerContactNo;

    @Column(name = "RetailerAddress",  length = 45 )
    private String RetailerAddress;

    @Column(name = "RetailerEmail",  length = 45 )
    private String RetailerEmail;

    @OneToMany(mappedBy = "retailer", cascade = CascadeType.ALL)
    @Column(name = "BillNO")
    private List<Bill> bills;

}
