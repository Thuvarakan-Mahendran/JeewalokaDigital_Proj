package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.BillDTO;
import com.jeewaloka.digital.jeewalokadigital.entity.bill.Bill;

import java.time.LocalDateTime;
import java.util.List;

public interface BillService {
    List<BillDTO> findByDate(LocalDateTime date);
    List<BillDTO> searchByTerm(String searchTerm);
    BillDTO addBill(BillDTO billDTO);
}
