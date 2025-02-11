package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Supplier")
@EntityListeners(AuditingEntityListener.class)
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SupplierID")
    private Long supplierId;

    @Column(name = "SupplierCode", unique = true, nullable = false, updatable = false)
    private String supplierCode;


    @Column(name = "Name", nullable = false)
    private String supplierName;

    @Column(name = "Contact")
    private String supplierContact;

    @Column(name = "Email", unique = true)
    private String supplierEmail;

    @Column(name = "Address")
    private String supplierAddress;

    @Column(name = "Fax")
    private String supplierFax;

    @Column(name = "Website")
    private String supplierWebsite;

    @Column(name = "Status")
    private String supplierStatus;

    @CreatedDate
    @Column(name = "CreatedDate", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;
}
