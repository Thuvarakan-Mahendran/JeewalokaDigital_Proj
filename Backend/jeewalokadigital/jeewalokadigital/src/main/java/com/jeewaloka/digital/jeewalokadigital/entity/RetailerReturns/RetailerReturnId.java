package com.jeewaloka.digital.jeewalokadigital.entity.RetailerReturns;

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

public class RetailerReturnId implements Serializable {

    @Column(name = "RetailerReturnedBillId", nullable = false, length = 10)
    private int retailerReturnedBillId;

    @Column(name = "RetailerReturnedItemId", nullable = false, length = 10)
    private int retailerReturnedItemId;
}
