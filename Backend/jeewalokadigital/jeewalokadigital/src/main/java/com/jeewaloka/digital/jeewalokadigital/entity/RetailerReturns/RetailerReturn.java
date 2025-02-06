package com.jeewaloka.digital.jeewalokadigital.entity.RetailerReturns;

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
@Table(name = "RetailerReturn")

public class RetailerReturn {

    @EmbeddedId
    private RetailerReturnId id;

    @Column(name ="RetailerReturnItemName", nullable = false, length = 50)
    private String retailerReturnItemName;

    @Column(name = "Quantity", nullable = false, length = 10)
    private int quantity;


}
