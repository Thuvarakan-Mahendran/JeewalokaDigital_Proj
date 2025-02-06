package com.jeewaloka.digital.jeewalokadigital.entity.SupplierReturns;

import com.jeewaloka.digital.jeewalokadigital.entity.RetailerReturns.RetailerCleanedId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "SupplierCleaned")

public class SupplierCleaned {

    @EmbeddedId
    private SupplierCleanedId id;

    @Column(name = "SupplierClearedItemName", nullable = false, length = 10)
    private String supplierClearedItemName;

    @Column(name = "Quantity", nullable = false, length = 10)
    private int quantity;



}
