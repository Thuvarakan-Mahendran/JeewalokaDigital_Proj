package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface BillService {
    BillResponseDTO addBill(BillRequestDTO billDTO);
    List<BillResponseDTO> findAllBills();
}
