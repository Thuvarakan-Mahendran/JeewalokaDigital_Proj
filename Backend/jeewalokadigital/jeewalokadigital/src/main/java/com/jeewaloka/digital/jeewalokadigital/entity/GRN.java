package com.jeewaloka.digital.jeewalokadigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "grn")
public class GRN {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long grnId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier grnSupplier;

    @Column(nullable = false)
    private String grnReceivedBy;

    @Column(nullable = false)
    private Double grnTotalAmount;

    @Column(nullable = false)
    private String grnStatus;


    @CreatedDate
    private LocalDate grnCreatedDate;


    @LastModifiedDate
    private LocalDate grnUpdatedDate;

    @OneToMany(mappedBy = "grn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GRNItem> grnItems = new ArrayList<>();

}