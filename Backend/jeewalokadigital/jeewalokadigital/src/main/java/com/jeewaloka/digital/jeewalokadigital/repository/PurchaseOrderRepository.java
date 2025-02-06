package com.jeewaloka.digital.jeewalokadigital.repository;

import com.jeewaloka.digital.jeewalokadigital.entity.GRN;
import com.jeewaloka.digital.jeewalokadigital.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {

}
