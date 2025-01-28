package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.BillDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface BillService {
    List<BillDTO> findByDate(LocalDateTime date);
    List<BillDTO> searchByTerm(String searchTerm);
}
