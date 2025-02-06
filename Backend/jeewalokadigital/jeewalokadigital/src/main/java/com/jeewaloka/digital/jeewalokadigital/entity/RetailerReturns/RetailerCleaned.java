package com.jeewaloka.digital.jeewalokadigital.entity.RetailerReturns;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "RetailerCleaned")

public class RetailerCleaned {


    @EmbeddedId
    private RetailerCleanedId id;


    @Column(name = "RetailerClearedItemName", nullable = false, length = 10)
    private  String retailerClearedItemName;

    @Column(name = "Quantity", nullable = false, length = 10)
    private int quantity;

}
