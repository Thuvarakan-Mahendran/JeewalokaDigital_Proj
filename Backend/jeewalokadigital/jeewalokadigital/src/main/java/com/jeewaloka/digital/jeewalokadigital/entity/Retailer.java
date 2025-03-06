package com.jeewaloka.digital.jeewalokadigital.entity;

import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "retailer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Retailer {
    @Id
    @Column(name = "RetailerId", length = 80)  // is it good to define RestailerId as retailerId?
    private String RetailerId;

    @Column(name = "RetailerName", length = 45 )
    private String RetailerName;

    @Column(name = "RetailerContactNo",  length = 10 )
    private String RetailerContactNo;

    @Column(name = "RetailerAddress",  length = 100 )
    private String RetailerAddress;

    @Column(name = "RetailerEmail",  length = 45 )
    private String RetailerEmail;

    @OneToMany(mappedBy = "retailer", cascade = CascadeType.ALL)
    @Column(name = "BillNO")
    private List<Bill> bills;

}
