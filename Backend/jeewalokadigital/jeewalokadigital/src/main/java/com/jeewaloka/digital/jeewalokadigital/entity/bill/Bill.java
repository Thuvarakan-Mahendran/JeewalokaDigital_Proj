package com.jeewaloka.digital.jeewalokadigital.entity.bill;

import com.jeewaloka.digital.jeewalokadigital.enums.BillCategory;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public abstract class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idSeq")
    @SequenceGenerator(name = "idSeq", sequenceName = "idSeq", allocationSize = 1)
    private Long BillNO;
    private Long UID;
    private Long RID;
    private Float total;
    private LocalDateTime date;
    private BillCategory billCategory;
    @OneToMany(mappedBy = "bill",cascade = CascadeType.ALL)
    private List<BillItem> billItems;

}
