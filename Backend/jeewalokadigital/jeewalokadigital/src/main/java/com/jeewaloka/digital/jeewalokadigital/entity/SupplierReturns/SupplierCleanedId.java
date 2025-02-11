//package com.jeewaloka.digital.jeewalokadigital.entity.SupplierReturns;
//
//import com.jeewaloka.digital.jeewalokadigital.entity.Item;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
//@Embeddable
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@EqualsAndHashCode
//
//public class SupplierCleanedId implements Serializable {
//
//    @Column(name = "SupplierReturnedBillId", nullable = false, length = 10)
//    private int supplierReturnedBillId;
//
//    @Column(name = "SupplierClearedItemId", nullable = false, length = 10)
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinColumn()
//    private Item supplierClearedItemId;
//
//}
