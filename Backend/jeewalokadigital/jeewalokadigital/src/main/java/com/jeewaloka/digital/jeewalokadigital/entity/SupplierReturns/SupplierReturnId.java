package com.jeewaloka.digital.jeewalokadigital.entity.SupplierReturns;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode


public class SupplierReturnId implements Serializable {

    @Column(name = "SupplierReturnedBillId", nullable = false, length = 10)
    private int supplierReturnedBillId;

    @Column(name = "SupplierReturnedItemId", nullable = false, length = 10)
    private int supplierReturnedItemId;

    @Column(name = "PC_code", nullable = false, length = 10)
    private int pcCode;
}
