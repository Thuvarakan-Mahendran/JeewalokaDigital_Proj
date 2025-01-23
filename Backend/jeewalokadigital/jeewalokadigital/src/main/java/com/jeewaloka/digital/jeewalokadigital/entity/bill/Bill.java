package com.jeewaloka.digital.jeewalokadigital.entity.bill;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "BillType", discriminatorType = DiscriminatorType.STRING)
public abstract class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSeq")
    @SequenceGenerator(name = "idSeq", sequenceName = "idSeq", allocationSize = 1)
    private Long BillNO;
    private Long UID;
    private Long RID;
    private Long ICODE;
    private String values;
    private Integer quantity;
    private Float total;
    private LocalDateTime date;
}
