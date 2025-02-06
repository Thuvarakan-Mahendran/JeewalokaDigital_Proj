package com.jeewaloka.digital.jeewalokadigital.entity.SupplierReturns;

import com.jeewaloka.digital.jeewalokadigital.entity.Item;
import com.jeewaloka.digital.jeewalokadigital.entity.RetailerReturns.RetailerReturnId;
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
@Table(name = "SupplierReturn")
public class SupplierReturn {

    @EmbeddedId
    private SupplierReturnId id;

    @Column(name ="SupplierReturnItemName", nullable = false, length = 50)
    private Item supplierReturnItemName;

    @Column(name = "Quantity", nullable = false, length = 10)
    private int quantity;
}
