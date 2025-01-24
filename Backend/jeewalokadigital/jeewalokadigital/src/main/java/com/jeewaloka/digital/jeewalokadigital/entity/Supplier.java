package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "Supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SupplierID")
    private Long supplierId;

//    @ManyToOne
//    @JoinColumn(name = "UserID", nullable = false)
//    private User user;

    @Column(name = "Name", nullable = false)
    private String supplierName;

    @Column(name = "Contact")
    private String supplierContact;

    @Column(name = "Email", unique = true)
    private String supplierEmail;

    @Column(name = "Address")
    private String supplierAddress;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;


}