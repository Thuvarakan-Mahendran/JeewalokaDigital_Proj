package com.jeewaloka.digital.jeewalokadigital.entity.bill;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
@Entity
@DiscriminatorValue("CASH")
public class CashBill extends Bill{

}
