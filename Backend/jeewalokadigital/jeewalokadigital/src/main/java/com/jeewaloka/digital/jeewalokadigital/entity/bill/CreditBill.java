package com.jeewaloka.digital.jeewalokadigital.entity.bill;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CREDIT")
public class CreditBill extends Bill{
    private Float paidAmount;

}
