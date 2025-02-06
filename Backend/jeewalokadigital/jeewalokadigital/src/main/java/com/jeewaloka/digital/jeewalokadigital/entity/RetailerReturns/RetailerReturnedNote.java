package com.jeewaloka.digital.jeewalokadigital.entity.RetailerReturns;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "RetailerReturnedNote")
public class RetailerReturnedNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rbillId", nullable = false, length = 10)
    private Integer rbillId;

    @Column(name = "date", nullable = false, length = 10)
    private Date date;

    @Column(name = "ReturnTotal", nullable = false, length = 10)
    private long ReturnTotal;

    @Column(name = "ClearedTotal", nullable = false, length = 10)
    private long ClearedTotal;
}
